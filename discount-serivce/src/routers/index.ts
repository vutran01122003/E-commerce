import { Router } from "express";
import discountRouter from "./discount";

const router: Router = Router();

router.use(discountRouter);

export default router;
