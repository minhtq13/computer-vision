import Cookies from "js-cookie";

export const ArrayCookieUtil = {
  set: (key: string, array: string[]) => {
    try {
      Cookies.set(key, JSON.stringify(array));
      return true;
    } catch (error) {
      console.error("Error setting array cookie:", error);
      return false;
    }
  },

  get: (key: string): string[] => {
    try {
      const value = Cookies.get(key);
      return value ? JSON.parse(value) : [];
    } catch (error) {
      console.error("Error getting array cookie:", error);
      return [];
    }
  },

  update: (key: string, newArray: string[]) => {
    ArrayCookieUtil.set(key, newArray);
  },

  append: (key: string, item: string) => {
    const currentArray = ArrayCookieUtil.get(key);
    ArrayCookieUtil.set(key, [...currentArray, item]);
  },

  remove: (key: string) => {
    Cookies.remove(key);
  },
};
export const getItemCookie = (key: string) => {
  return Cookies.get(key);
};
export const setItemCookie = (key: string, value: string) => {
  Cookies.set(key, value);
};

export const removeItemCookie = (key: string) => {
  Cookies.remove(key);
};
