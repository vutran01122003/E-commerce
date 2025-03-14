import { type Request, type Response, type NextFunction } from "express";
import createError from "http-errors";
import { DiscountInput, UpdatedDiscountInput } from "../validation/discount.schema";
import DiscountService from "../services/discount.service";
import { Code } from "../shared/code";
import { DiscountDocument } from "../models/discount.model";

class DiscountController {
    async createDiscountHandler(req: Request<{}, {}, DiscountInput["body"]>, res: Response, next: NextFunction) {
        try {
            const discountData: DiscountInput["body"] = req.body;

            const discount = await DiscountService.createDiscount(discountData);

            res.status(201).json({
                message: "Create discount successful",
                data: discount,
                code: Code.SUCCESS
            });
        } catch (error) {
            next(error);
        }
    }

    async getDiscountHandler(req: Request, res: Response, next: NextFunction) {
        try {
            const dicountId: string = req.params.dicountId;

            const discount = await DiscountService.getDiscount(dicountId);

            res.status(200).json({
                message: "Get discount successful",
                data: discount,
                code: Code.SUCCESS
            });
        } catch (error) {
            next(error);
        }
    }

    async updateDiscountHandler(
        req: Request<UpdatedDiscountInput["params"], {}, UpdatedDiscountInput["body"]>,
        res: Response,
        next: NextFunction
    ) {
        try {
            const discountId: string = req.params.discountId;
            const updateDiscountData: UpdatedDiscountInput["body"] = req.body;

            const updatedDiscount: DiscountDocument = await DiscountService.updateDiscount(
                discountId,
                updateDiscountData
            );

            res.status(200).json({
                message: "Update discount successful",
                data: updatedDiscount,
                code: Code.SUCCESS
            });
        } catch (error) {
            next(error);
        }
    }

    async deleteDiscountHandler(req: Request, res: Response, next: NextFunction) {
        try {
            const discountId: string = req.params.discountId;

            if (!discountId) throw createError.BadRequest("Invalid discount id");

            await DiscountService.deleteDiscount(discountId);

            res.status(200).json({
                message: "Delete discount successful",
                code: Code.SUCCESS
            });
        } catch (error) {
            next(error);
        }
    }

    async getDiscountsByShopHandler(req: Request, res: Response, next: NextFunction) {
        try {
            const shopId: string = req.params.shopId;

            if (!shopId) throw createError.BadRequest("Invalid shop id");

            const discountList = await DiscountService.getDiscountsByShop(shopId);

            res.status(200).json({
                code: Code.SUCCESS,
                message: "Get discounts by shop successful",
                data: discountList
            });
        } catch (error) {
            next(error);
        }
    }
}

export default new DiscountController();
