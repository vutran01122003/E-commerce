import express, { Express, type NextFunction, type Request, type Response } from "express";
import { Env } from "./shared";
import logger from "./utils/logger";
import compression from "compression";
import router from "./routers";
import defaultConfig from "./config/default.config";
import { ZodError } from "zod";
import { Code } from "./shared/code";

const app: Express = express();
const { ENV } = defaultConfig;

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(compression());

if (ENV === Env.DEV) {
    app.use((req: Request, res: Response, next: NextFunction) => {
        logger.debug(`${req.method}: ${req.url}`);
        next();
    });
}

app.use("/api", router);

app.use((req: Request, res: Response, next: NextFunction) => {
    res.status(400).json({
        message: "Route does not exist",
        code: 400
    });
});

app.use((err: any, req: Request, res: Response, next: NextFunction) => {
    logger.error(JSON.stringify(err));

    if (err instanceof ZodError) {
        res.status(400).json({
            status: 400,
            code: Code.ZOD_ERROR,
            message: err.issues[0].message
        });
    } else {
        const message: String = err.message || "Internal error occurs";
        const status: number = err.status || 500;

        res.status(status).json({
            code: Code.UNCATEGORIZED_ERROR,
            status,
            message
        });
    }
});

export default app;
