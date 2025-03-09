import mongoose, { Connection, Mongoose } from "mongoose";
import { DBType } from "../shared";
import logger from "../utils/logger";
import Redis from "ioredis";
import defaultConfig from "../config/default.config";

const {
    MONGODB: { URI }
} = defaultConfig;

class Database {
    static instances: Database[] = [];
    private connection!: Connection | Redis;
    private dbType?: string;

    constructor(dbType: string) {
        this.onConnect(dbType);
    }

    public onConnect(dbType: string) {
        this.dbType = dbType;

        switch (dbType) {
            case DBType.MONGODB: {
                this.connection = mongoose.createConnection(URI);

                this.connection.on("connected", function (this: Connection) {
                    logger.debug(`MONGODB: ${this.name} connected`);
                });

                this.connection.on("error", (error) => {
                    logger.error("MONGODB: error", JSON.stringify(error));
                    throw error;
                });

                this.connection.on("disconnected", function (this: Connection) {
                    logger.debug(`MONGODB: ${this.name} disconnected`);
                });

                break;
            }

            case DBType.REDIS: {
                this.connection = new Redis({
                    port: 6379,
                    host: "127.0.0.1"
                });

                this.connection.on("connect", function () {
                    logger.debug(`Redis connected`);
                });

                break;
            }

            default: {
                logger.error("Invalid database type");
                break;
            }
        }
    }

    public static getInstance(dbType: string): Database {
        let database: Database;

        for (let i = 0; i < Database.instances.length; i++) {
            if (Database.instances[i].dbType === dbType) {
                return Database.instances[i];
            }
        }

        database = new Database(dbType);
        Database.instances.push(database);

        return database;
    }

    public getConnection(): Connection | Redis | null {
        if (!this.connection) return null;
        return this.connection;
    }
}

export default Database;
