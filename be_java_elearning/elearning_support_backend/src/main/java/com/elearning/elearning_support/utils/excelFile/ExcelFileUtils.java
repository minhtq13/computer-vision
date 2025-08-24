package com.elearning.elearning_support.utils.excelFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.sql.DataSource;
import org.apache.commons.math3.util.Pair;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.object.ObjectUtils;
import com.elearning.elearning_support.utils.springCustom.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ExcelFileUtils {

    private static final SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.FORMAT_YYYY_MM_DD);

    /**
     * Map<Integer, Pair<String, String>> = {Integer : columnIdx, Pair<headerName, methodName>}
     */
    public <T> InputStreamResource createWorkbook(List<T> lstObject, Map<Integer, Pair<String, String>> structure, String sheetName) throws IOException {
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(lstObject)) {
            return null;
        }
        long startTime = System.currentTimeMillis();
        log.info("createWorkbook: start at {}, lstObj {} items", startTime, lstObject.size());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            workbook.setCompressTempFiles(true);

            // Create data sheet
            SXSSFSheet sheet = workbook.createSheet(sheetName);
            // prevent overload in memory by fix size of accessible data kept in memory in any time
            sheet.setRandomAccessWindowSize(100);
            try {
                Field _sh = SXSSFSheet.class.getDeclaredField("_sh");
                _sh.setAccessible(true);
                XSSFSheet xssfSheet = (XSSFSheet) _sh.get(sheet);
                CellRangeAddress cellRange = new CellRangeAddress(0, lstObject.size(), 0, structure.size());
                xssfSheet.addIgnoredErrors(cellRange, IgnoredErrorType.NUMBER_STORED_AS_TEXT);
            } catch (NoSuchFieldException | IllegalAccessException exception) {
                log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
            }

            // fill data to sheet
            fillDataToSheet(sheet, lstObject, structure);

            // Write data to output stream
            workbook.write(outputStream);
        }
        outputStream.close(); // close stream to prevent memory leaking
        log.info("createWorkbook: end after {}, lstObj {} items", System.currentTimeMillis() - startTime, lstObject.size());
        return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
    }

    /**
     * Fill data to a sheet
     */
    public <T> void fillDataToSheet(SXSSFSheet sheet, List<T> lstObject, Map<Integer, Pair<String, String>> structure) {
        int columnSize = structure.size();
        sheet.trackAllColumnsForAutoSizing();
        // create a map between columnIdx and methodName
        Map<Integer, String> mapFieldIdx = new HashMap<>();
        SXSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("STT");
        // Init mapFieldIdx and create other columns of the header
        for (int i = 1; i <= columnSize; i++) {
            Pair<String, String> colStructure = structure.get(i);
            headerRow.createCell(i).setCellValue(colStructure.getKey());
            mapFieldIdx.put(i, colStructure.getValue());
        }

        // Fill data
        int rowIdx = 1;
        // get classType of item => using reflection => invoke method
        Class<?> clazzT = lstObject.get(0).getClass();
        for (T item : lstObject) {
            SXSSFRow row = sheet.createRow(rowIdx);
            row.createCell(0, CellType.NUMERIC).setCellValue(rowIdx); // No cell
            for (int i = 1; i <= columnSize; i++) {
                try {
                    Method invokeMethod = clazzT.getDeclaredMethod(mapFieldIdx.get(i));
                    String cellValue = Objects.toString(invokeMethod.invoke(item), Strings.EMPTY);
                    row.createCell(i, CellType.STRING).setCellValue(ObjectUtils.getOrDefault(cellValue, ""));
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
                    log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
                }
                sheet.autoSizeColumn(i);
            }
            rowIdx++;
        }
    }

    /**
     * Get Response Stream CSV from InputStreamResource
     */
    public static ResponseEntity<InputStreamResource> getResponseCSVStream(InputStreamResource inputStreamResource) {
        String fileName = String.format("Export_surgery_%s.csv",
            new SimpleDateFormat(DateUtils.FORMAT_DATE_YYYY_MMDD_HHMMSS).format(new Date()));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachments; filename=" + fileName)
            .contentType(MediaType.parseMediaType("text/csv; charset=utf-8"))
            .body(inputStreamResource);
    }


    /**
     * createCSVFile from sql query
     */
    public static InputStreamResource createCSVFile(String headerQuery, String contentQuery) throws SQLException {
        DataSource dataSource = SpringContextUtils.getBean(DataSource.class);
        Connection connection = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            connection = dataSource.getConnection();
            CopyManager copyManager = new CopyManager((BaseConnection) connection.unwrap(PGConnection.class));
            outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}); // Add UTF-8 BOM marker bytes
            copyManager.copyOut("COPY ((" + headerQuery + ") UNION ALL (" + contentQuery + ")) TO STDOUT WITH (FORMAT CSV, ENCODING UTF8)",
                outputStream);
        } catch (Exception e) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
        } finally {
            if (Objects.nonNull(connection)) {
                connection.close();
            }
        }
        return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
    }

    /********************************************************************************************************************************************
     * Hỗ trợ import file excel từ database
     *******************************************************************************************************************************************/

    public static String getStringCellValue(Cell cell) {
        if (Objects.isNull(cell)) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) { // check cell date format
                    return formatter.format(cell.getDateCellValue());
                } else {
                    double cellNumValue = cell.getNumericCellValue();
                    return cellNumValue != Math.floor(cellNumValue) ? String.valueOf(cellNumValue).trim()
                        : String.format("%.0f", cellNumValue).trim(); // check cell number type
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()).trim();
            case FORMULA:
                return cell.getCellFormula().trim();
            default:
                return "";
        }
    }

    /**
     * Tạo cell với cellStyle và cellType
     */
    public static void createCellWithCellStyle(XSSFRow row, Integer cellIdx, CellStyle cellStyle, String cellValue) {
        XSSFCell newCell = row.createCell(cellIdx, CellType.STRING);
        newCell.setCellStyle(cellStyle);
        newCell.setCellValue(cellValue);
    }

    /**
     * Set border cell
     */
    public static void createBorderCellStyle(CellStyle cellStyle, BorderStyle borderStyle) {
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderRight(borderStyle);
    }


    /**
     * Format error excel file name
     */
    public static String getErrorFileName(String originalFileName, String errorFileName) {
        return String.format("%s_%s_%s.xlsx", originalFileName, errorFileName,
            new SimpleDateFormat(DateUtils.FORMAT_DATE_FILE_DD_MM_YYYY_HH_MM_SS).format(new Date()));
    }

}
