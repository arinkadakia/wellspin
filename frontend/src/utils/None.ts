export const None = <T>(value: T | null | undefined): value is undefined => {
  return value == null;
};
