import { atom } from "recoil";
import { UserType } from "../pages/MyPage";
export const isDeleteMode = atom<boolean>({
  key: "DeleteModeState",
  default: false,
});

export const isEditMode = atom<boolean>({
  key: "EditModeState",
  default: false,
});
export const UserData = atom<UserType | null>({
  key: "UserData",
  default: null,
});
