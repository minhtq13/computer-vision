import moment from "moment-timezone";

// convert a date to string with specified pattern
export const formatDateToString = (sourceDate, pattern) => {
    try {
        if (sourceDate instanceof Date) {
            return moment(sourceDate).format(pattern);
        } else {
            return undefined;
        }
    } catch (err) {
        return undefined;
    }
}

// parse a string to date
export const parseStringToDate = (dateString, pattern) => {
    try {
        if (typeof(dateString) === "string") {
            return moment(dateString, pattern).toDate();
        } else {
            return undefined;
        }
    } catch (err) {
        return undefined;
    }
}