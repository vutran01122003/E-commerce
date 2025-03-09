import mongoose from "mongoose";

export const validateObjectId = (value: string): boolean => {
    const ObjectId = mongoose.Types.ObjectId;
    return ObjectId.isValid(value);
};
