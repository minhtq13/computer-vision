type ObjectType = { [key: string]: any };

export const fromJsonToObject = <T = ObjectType>(jsonData: string): T | null => {
  let returnValue: null | T = null;
  try {
    returnValue = JSON.parse(jsonData);
  } catch (e) {
    console.log(e);
  }
  return returnValue;
};

export const fromObjectToJson = (objectData: { [key: string]: any }): string | null => {
  let returnValue: null | string = null;
  try {
    returnValue = JSON.stringify(objectData);
  } catch (e) {
    console.log(e);
  }
  return returnValue;
};
