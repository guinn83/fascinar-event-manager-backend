import authService from "./auth.service";

export default function authHeader() {
    if (authService.isSigned()) {
        return { "Authorization": authService.getToken() };
    } else {
      return {};
    }
  }