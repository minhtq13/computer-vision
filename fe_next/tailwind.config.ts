import type { Config } from "tailwindcss";

const config: Config = {
  content: ["./src/**/*.{js,ts,jsx,tsx,mdx}"],
  theme: {
    extend: {
      fontFamily: {
        roboto: "var(--font-roboto)",
      },
      colors: {
        border: {
          disable: "var(--border-disable)",
        },
        background: {
          disable: "var(--bg-disable)",
        },
        hust: {
          DEFAULT: "var(--hust-color)",
          10: "var(--hust-color-10)",
          20: "var(--hust-color-20)",
          30: "var(--hust-color-30)",
          40: "var(--hust-color-40)",
          50: "var(--hust-color-50)",
          60: "var(--hust-color-60)",
          70: "var(--hust-color-70)",
          80: "var(--hust-color-80)",
          90: "var(--hust-color-90)",
        },
        "disable-secondary": "var(--disable-secondary)",
        "container-secondary": "var(--container-secondary)",
        shadow: {
          primary: "var(--box-shadow-primary)",
        },
        success: {
          DEFAULT: "var(--color-sematic-success)",
          interactive: {
            1: "var(--color-sematic-success-interactive-1)",
          },
        },
        error: {
          DEFAULT: "var(--color-sematic-error)",
          interactive: {
            1: "var(--color-sematic-error-interactive-1)",
            2: "var(--color-sematic-error-interactive-2)",
          },
        },
        warning: {
          interactive: {
            1: "var(--color-sematic-warning-interactive-1)",
          },
        },
        orchid: {
          DEFAULT: "var(--color-sematic-purple-orchid)",
        },
        text: {
          primary: {
            DEFAULT: "var(--color-text-primary-1)",
            white: "var(--color-text-white)",
            1: "var(--color-text-primary-1)",
            2: "var(--color-text-primary-2)",
            3: "var(--color-text-secondary-3)",
            4: "var(--color-text-primary-4)",
            semi: "var(--color-text-primary-semi)",
          },
          hust: "var(--hust-color)",
          counter: {
            primary: {
              2: "var(--color-text-counter-primary-2)",
            },
          },
          secondary: {
            DEFAULT: "var(--color-text-secondary)",
            1: "var(--color-text-secondary-1)",
            2: "var(--color-text-secondary-2)",
          },
          disable: "var(--color-text-disable)",
          tertiary: "var(--color-text-tertiary)",
          black: "var(--color-text-black-primary)",
          neon: {
            1: "var(--color-text-neon-primary-1)",
            primary: "var(--color-neon-primary-1)",
          },
        },
        primary: {
          default: "var(--color-primary-default)",
          1: "var(--color-primary-1)",
          pressed: "var(--color-primary-pressed)",
          neon: "var(--color-fill-neon)",
          semi: "var(--bg-primary-semi)",
        },
        fill: {
          success: "var(--color-fill-success)",
          error: "var(--color-fill-error)",
          warning: "var(--color-fill-warning)",
          orchid: "var(--color-fill-orchid)",
        },
        container: {
          primary: {
            DEFAULT: "var(--color-container-primary)",
            2: "var(--color-stroke-primary-2)",
            3: "var(--color-container-primary-1)",
            4: "var(--color-container-primary-4)",
          },
          secondary: {
            DEFAULT: "var(--color-container-secondary)",
            1: "var(--color-container-secondary-1)",
            2: "var(--color-bg-chip-1)",
            3: "var(--color-container-secondary-3)",
          },
          tertiary: "var(--color-container-tertiary)",
          highlight: {
            1: "var(--color-container-highlight-1)",
          },
          disabled: "var(--color-container-disabled)",
          surface: {
            primary: "var(--color-surface-container-primary)",
            secondary: "var(--color-surface-container-secondary)",
            tertiary: "var(--color-surface-container-tertiary)",
            1: "var(--color-surface-container-secondary-1)",
          },
        },
        alternative: {
          hover: "var(--color-alternative-hover)",
          pressed: "var(--color-alternative-pressed)",
        },
      },
      screens: {
        "2xs": "0px",
        xs: "375px",
        sm: "640px",
        md: "768px",
        "2md": "896px",
        lg: "1024px",
        xl: "1280px",
        "2xl": "1440px",
      },
    },
  },
  plugins: [],
};
export default config;
