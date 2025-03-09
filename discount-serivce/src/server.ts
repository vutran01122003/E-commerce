import app from "./app";
import logger from "./utils/logger";
import defaultConfig from "./config/default.config";

const { PORT } = defaultConfig;

app.listen(PORT, () => {
    logger.info(`[server]: Server is running at http://localhost:${PORT}`);
});
