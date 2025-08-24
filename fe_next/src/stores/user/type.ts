export type PLogin = {
  username: string;
  password: string;
};
export type LoginRes = {
  roles: string[];
  permissions: string[];
  accessToken: string;
  refreshToken: string;
  issuedAt: string;
};
