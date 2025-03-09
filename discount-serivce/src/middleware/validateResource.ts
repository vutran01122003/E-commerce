import { type Request, type Response, type NextFunction } from "express";
import { ZodSchema } from "zod";

const validateResource = (schema: ZodSchema) => {
    return (req: Request, res: Response, next: NextFunction) => {
        try {
            schema.parse({
                body: req.body,
                params: req.params,
                query: req.query
            });
            next();
        } catch (error) {
            next(error);
        }
    };
};

export default validateResource;
