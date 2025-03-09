import { Connection, Document, Schema } from "mongoose";
import Database from "../databases/database.connection";
import { DBType } from "../shared";

const [DOC, COL] = ["dicount", "discounts"];
const conn = Database.getInstance(DBType.MONGODB).getConnection() as Connection;

export interface DiscountDocument extends Document {
    shop: Schema.Types.ObjectId;
    name: string;
    code: string;
    start_time: Date;
    expiry_time: Date;
    discount_type: string;
    discount_value: number;
    min_price_product: number;
    quantity: number;
    quantity_per_user: number;
    used_user_list: Schema.Types.ObjectId[];
    applied_product_type: string;
    applied_product_list: Schema.Types.ObjectId[];
    is_private: boolean;
    is_active: boolean;
}

const DiscountSchema = new Schema<DiscountDocument>(
    {
        shop: {
            type: Schema.Types.ObjectId
        },
        name: {
            type: String,
            minLength: 2,
            maxLength: 255
        },
        code: {
            type: String,
            minLength: 5,
            maxLength: 255
        },
        start_time: {
            type: Date
        },
        expiry_time: {
            type: Date
        },
        discount_type: {
            type: String,
            enum: ["fixed", "percentage"],
            default: "fixed"
        },
        discount_value: {
            type: Number,
            default: 0
        },
        min_price_product: {
            type: Number
        },
        quantity: {
            type: Number,
            default: 0
        },
        quantity_per_user: {
            type: Number,
            default: 0
        },
        used_user_list: {
            type: [Schema.Types.ObjectId],
            default: []
        },
        applied_product_type: {
            type: String,
            enum: ["all", "specific"]
        },
        applied_product_list: {
            type: [Schema.Types.ObjectId],
            default: []
        },
        is_private: {
            type: Boolean,
            default: false
        },
        is_active: {
            type: Boolean,
            default: true
        }
    },
    {
        timestamps: true,
        collection: COL
    }
);

const Discount = conn.model<DiscountDocument>(DOC, DiscountSchema);

export default Discount;
