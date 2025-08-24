const {resolve} = require("path");

module.exports = {
  module: {
    rules: [
      {
        test: /\.js$/,
        enforce: "pre",
        use: ["source-map-loader"],
      },
    ],
  },
  resolve: {
    alias : {
      'html2canvas': resolve(__dirname, 'node_modules/html2canvas-pro')
    }
  },
  ignoreWarnings: [/Failed to parse source map/],
};