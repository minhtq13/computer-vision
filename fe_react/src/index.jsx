import React from "react";
import ReactDOM from "react-dom/client";

import {Provider} from "react-redux";
import GlobalStyles from "./components/GlobalStyles";
import App from "./App";
import store from "./redux/store";
import {ConfigProvider} from "antd";
import {HUST_COLOR} from "./utils/constant";

const root = ReactDOM.createRoot(document.getElementById("root"));
const app = (
    <Provider store={store}>
    <ConfigProvider
        theme={{
            token: {
                colorPrimary: HUST_COLOR,
            },
        }}
    >
        <GlobalStyles>
            <App/>
        </GlobalStyles>
    </ConfigProvider>
    </Provider>)
root.render(
    process.env.NODE_ENV === "production"
        ? <React.StrictMode>{app}</React.StrictMode>
        : <>{app}</>
);
