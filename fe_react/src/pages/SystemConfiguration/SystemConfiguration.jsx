import React from "react";
import "./SystemConfiguration.scss"
import {Switch} from "antd";
const SystemConfiguration = () => {
    return (
      <div className="system-config-container">
          <div className="header-system-config">
              <p>Cấu hình hệ thống</p>
          </div>
          <div className="config-container">
              <div className="config-item">
                  <p>Cấu hình bật/tắt reCaptcha</p>
                  <Switch
                      defaultValue={false}
                  >
                  </Switch>
              </div>
          </div>
      </div>
    );
}
export default SystemConfiguration;