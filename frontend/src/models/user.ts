export type UserModel = {
    id: number;
    username: string;
    password: string;
    userRole: string;
    locked?: boolean;
    enabled?: boolean;
}