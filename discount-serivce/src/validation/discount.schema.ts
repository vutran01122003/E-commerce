import { object, string, number, boolean, TypeOf } from "zod";
import { validateObjectId } from "../utils";

export const DiscountSchema = object({
    body: object({
        shop: string({
            required_error: "Shop id reference must be required"
        }).refine((value) => validateObjectId(value), {
            message: "Invalid Shop id reference"
        }),
        name: string({
            required_error: "Discount name is required"
        })
            .min(2, "Discount name must be greater than or equal to 2 characters")
            .max(255, "Discount name must be less than or equal to 255 characters"),
        code: string({
            required_error: "Code is required"
        })
            .min(2, "Code must be greater than or equal to 2 characters")
            .max(255, "Code must be less than or equal to 255 characters"),
        start_time: string({
            required_error: "Start time is required"
        })
            .datetime()
            .refine((value) => new Date(value) > new Date(), {
                message: "Start time must be after the current time"
            }),
        expiry_time: string({
            required_error: "Expiry time is required"
        })
            .datetime()
            .refine((value) => new Date(value) > new Date(), {
                message: "Expiry time must be after the current time"
            }),
        discount_type: string({
            required_error: "Discount type is required"
        }),
        discount_value: number({
            required_error: "Discount value is required"
        }),
        min_price_product: number({
            required_error: "Min price product is required"
        }),
        quantity: number({
            required_error: "Quantity is required"
        }),
        quantity_per_user: number({
            required_error: "Quantity per user is required"
        }),
        used_user_list: string().array().optional(),
        applied_product_type: string({
            required_error: "Applied product type is required"
        }).optional(),
        applied_product_list: string().array().optional(),
        is_private: boolean().default(false),
        is_active: boolean().default(true)
    }).refine((data) => new Date(data.start_time) < new Date(data.expiry_time), {
        message: "Start time must be less than expiry time",
        path: ["start_time"]
    })
});

export const UpdateDiscountSchema = object({
    body: object({
        name: string({
            required_error: "Discount name is required"
        })
            .min(2, "Discount name must be greater than or equal to 2 characters")
            .max(255, "Discount name must be less than 256 characters")
            .optional(),
        code: string()
            .min(5, "Code must be greater than or equal to 5 characters")
            .max(255, "Code must be less than 256 characters")
            .optional(),
        start_time: string().datetime().optional(),
        expiry_time: string().datetime().optional(),
        discount_type: string().optional(),
        discount_value: number()
            .refine((value) => value >= 0, { message: "Discount value must be positive number" })
            .optional(),
        min_price_product: number()
            .refine((value) => value >= 0, { message: "Min price product must be positive number" })
            .optional(),
        quantity: number()
            .refine((value) => value >= 0, { message: "Quantity must be positive number" })
            .optional(),
        quantity_per_user: number()
            .refine((value) => value >= 0, { message: "Quantity per user must be positive number" })
            .optional(),
        applied_product_type: string().optional(),
        applied_product_list: string().array().optional(),
        is_private: boolean().default(false),
        is_active: boolean().default(true)
    }),
    params: object({
        discountId: string().refine((value) => validateObjectId(value), {
            message: "Invalid discount id"
        })
    })
});

export type DiscountInput = TypeOf<typeof DiscountSchema>;
export type UpdatedDiscountInput = TypeOf<typeof UpdateDiscountSchema>;
