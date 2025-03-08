import pino from "pino";

const logger = pino({
    level: "debug",
    transport: {
        targets: [
            {
                target: "pino-pretty",
                level: "debug"
            }
        ]
    }
});

export default logger;
