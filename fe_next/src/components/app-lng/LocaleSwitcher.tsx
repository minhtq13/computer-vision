import { Suspense } from "react";
import LocaleSwitcherSelect from "./LocaleSwitcherSelect";

export default function LocaleSwitcher() {
  return (
    <Suspense>
      <LocaleSwitcherSelect />
    </Suspense>
  );
}
