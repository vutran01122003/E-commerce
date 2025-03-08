import { object, string, number, date, boolean, TypeOf } from "zod";

export const DiscountSchema = object({
    body: object({
        shop: string({
            required_error: "Shop reference must be required"
        }),
        name: string({
            required_error: "Discount name is required"
        })
            .min(2, "Discount name must be greater than 1 characters")
            .max(255, "Discount name must be less than 256 characters"),
        code: string({
            required_error: "Code is required"
        })
            .min(2, "Code must be greater than 1 characters")
            .max(255, "Code must be less than 256 characters"),
        start_time: string({
            required_error: "Start time is required"
        }).datetime(),
        expiry_time: string({
            required_error: "Expiry time is required"
        }).datetime(),
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
        used_user_list: string().array(),
        applied_product_type: string({
            required_error: "Applied product type is required"
        }),
        applied_product_list: string().array(),
        is_private: boolean().default(false),
        is_active: boolean().default(true)
    })
});

export type DiscountInput = TypeOf<typeof DiscountSchema>["body"];
