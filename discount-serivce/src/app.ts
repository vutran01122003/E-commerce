import express, { Express, type NextFunction, type Request, type Response } from "express";
import dotenv from "dotenv";
import { env } from "./shared/env";
import logger from "./utils/logger";
dotenv.config();

const app: Express = express();

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

if (app.get("env") === env.DEV) {
    app.use((req: Request, res: Response, next: NextFunction) => {
        logger.debug(`${req.method}: ${req.url}`);
        next();
    });
}

app.get("/", (req: Request, res: Response) => {
    res.send("Express + TypeScript Server");
});

export default app;
