import { useRouter } from "next/navigation";

const useOpenNewTab = () => {
  const router = useRouter();
  const userAgent =
    typeof navigator !== "undefined" &&
    (navigator.userAgent || navigator.vendor || (window as any).opera);

  const openNewTab = (url: string) => {
    const isAndroid = /android/i.test(userAgent);
    const isIos =
      /iPad|iPhone|iPod/.test(userAgent) && !(window as any).MSStream;

    if (isAndroid) {
      router.push(url);
    } else if (isIos) {
      window.open(url, "_blank")?.focus();
    } else {
      window.open(url, "_blank")?.focus();
    }
  };

  return openNewTab;
};

export default useOpenNewTab;
