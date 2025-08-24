package com.elearning.elearning_support.constants;

public class FileConstants {

    public static final String[] EXTENSIONS = new String[]{
        Extension.Image.JPG,
        Extension.Image.JPEG,
        Extension.Image.PNG,
        Extension.Image.WEBP,
        Extension.Excel.XLS,
        Extension.Excel.XLSX,
        Extension.PortableDocumentFormat.PDF,
        Extension.Word.DOCX,
        Extension.Word.DOC,
        Extension.PowerPoint.PPT,
        Extension.PowerPoint.PPTX
    };

    public static class Extension {

        private Extension() {
        }

        public static class Excel {

            public static final String XLSX = "xlsx";
            public static final String XLS = "xls";

            private Excel() {
            }
        }

        public static class Word {

            public static final String DOCX = "docx";
            public static final String DOC = "doc";

            private Word() {
            }
        }

        public static class PortableDocumentFormat {

            public static final String PDF = "pdf";

            private PortableDocumentFormat() {
            }
        }

        public static class PowerPoint {

            public static final String PPT = "ppt";
            public static final String PPTX = "pptx";

            private PowerPoint() {
            }
        }

        public static class Image {

            public static final String PNG = "png";
            public static final String GIF = "gif";
            public static final String JPEG = "jpeg";
            public static final String JPG = "jpg";
            public static final String TIFF = "tiff";
            public static final String JFIF = "jfif";
            public static final String ICO = "ico";
            public static final String WEBP = "webp";

            private Image() {
            }
        }

        public static class Video {

            public static final String MKV = "mkv";
            public static final String FLV = "flv";
            public static final String AVI = "avi";
            public static final String MP4 = "mp4";
            public static final String MOV = "mov";
            public static final String WMV = "wmv";
            public static final String VOB = "vob";

            private Video() {
            }

        }


    }

}
