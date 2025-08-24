import { resolve } from "path";
import createNextIntlPlugin from "next-intl/plugin";

const withNextIntl = createNextIntlPlugin("./src/libs/i18n/request.ts");

/** @type {import('next').NextConfig} */
const nextConfig = {
  output: "standalone",
  webpack(config) {
    // Grab the existing rule that handles SVG imports
    const fileLoaderRule = config.module.rules.find((rule) => rule.test?.test?.(".svg"));

    config.module.rules.push(
      // Reapply the existing rule, but only for svg imports ending in ?url
      {
        ...fileLoaderRule,
        test: /\.svg$/i,
        resourceQuery: /url/, // *.svg?url
      },
      // Convert all other *.svg imports to React components
      {
        test: /\.svg$/i,
        issuer: fileLoaderRule.issuer,
        resourceQuery: { not: [...fileLoaderRule.resourceQuery.not, /url/] }, // exclude if *.svg?url
        use: ["@svgr/webpack"],
      }
    );
    // Modify the file loader rule to ignore *.svg, since we have it handled now.
    fileLoaderRule.exclude = /\.svg$/i;
    config.resolve.fallback = {
      // if you miss it, all the other options in fallback, specified
      // by next.js will be dropped.
      ...config.resolve.fallback,

      fs: false, // the solution
    };
    config.resolve.alias = {
      ...config.resolve.alias,
      html2canvas: resolve(import.meta.dirname, "node_modules/html2canvas-pro"),
    };

    return config;
  },
  experimental: {
    turbo: {
      resolveAlias: {
        html2canvas: "html2canvas-pro",
      },
    },
  },
  // experimental: {
  //   serverActions: true,
  // },
  // typescript: {
  //   ignoreBuildErrors: true,
  // },
  // eslint: {
  //   ignoreDuringBuilds: true,
  // },
  reactStrictMode: true,
  // headers: async () => {
  //   return [
  //     {
  //       source: "/manifest.webmanifest",
  //       headers: [
  //         {
  //           key: "Cache-Control",
  //           value: "no-cache, no-store, must-revalidate",
  //         },
  //       ],
  //     },
  //     {
  //       source: "/sw.js",
  //       headers: [
  //         {
  //           key: "Cache-Control",
  //           value: "no-cache, no-store, must-revalidate",
  //         },
  //       ],
  //     },
  //   ];
  // },
  // ...other config
};

export default withNextIntl(nextConfig);
