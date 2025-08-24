import { useClipboard, UseClipboardOptions } from "use-clipboard-copy";

export function useCopy(options?: UseClipboardOptions) {
  const clipboard = useClipboard({
    copiedTimeout: 2000,
    ...options,
  });
  return clipboard;
}
