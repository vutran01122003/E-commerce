import fs from "fs";
import { RedisKey } from "ioredis";
import JWT, { TokenExpiredError } from "jsonwebtoken";
import client from "../database/redis";
import ShopService from "./shop.service";
import _default from "../../config/default";
import { ShopDocument } from "../models/shop.model";
import { VerifyTokenResponse } from "../shared/types";
import { ReIssueTokenResponse } from "../shared/types";
import { BadRequestError, ForbiddenError, NotFoundError } from "../utils/response/error.response";

class JWTService {
    static async signToken(
        payload: Object,
        options: JWT.SignOptions,
        keyName: "ACCESS_KEY" | "REFRESH_KEY"
    ): Promise<string> {
        try {
            const keyValue = fs.readFileSync(
                `${__dirname}/../../keys/${keyName.toLocaleLowerCase()}/privateKey.pem`,
                {
                    encoding: "utf-8",
                }
            );
            const token = await JWT.sign(payload, keyValue, {
                ...(options && options),
                algorithm: "RS256",
            });
            return token;
        } catch (error) {
            throw error;
        }
    }

    static async verifyToken(
        tokenValue: string,
        keyName: "ACCESS_KEY" | "REFRESH_KEY"
    ): Promise<VerifyTokenResponse> {
        return new Promise((resolve, reject) => {
            try {
                const keyValue = fs.readFileSync(
                    `${__dirname}/../../keys/${keyName.toLocaleLowerCase()}/publicKey.crt`,
                    {
                        encoding: "utf-8",
                    }
                );

                JWT.verify(tokenValue, keyValue, (error, data) => {
                    if (error) {
                        resolve({
                            error,
                            isExpired: error instanceof TokenExpiredError,
                            userData: null,
                        });
                    }

                    resolve({
                        error: null,
                        isExpired: error instanceof TokenExpiredError,
                        userData: data as ShopDocument,
                    });
                });
            } catch (error) {
                reject(error);
            }
        });
    }

    static async reIssueAccessToken(refreshToken: string): Promise<ReIssueTokenResponse> {
        try {
            const { userData, error, isExpired }: VerifyTokenResponse = await this.verifyToken(
                refreshToken,
                "REFRESH_KEY"
            );

            if (isExpired) throw new ForbiddenError();
            if (error) throw error;

            const validRefreshToken = await client.get(`refreshToken:${userData!._id}`);

            if (validRefreshToken !== refreshToken) throw new BadRequestError("Invalid token");

            const user: Omit<ShopDocument, "password"> | null = await ShopService.findOne({
                _id: userData!._id,
            });

            if (!user) throw new NotFoundError("Shop does not exist");

            const accessToken: string = await this.signToken(
                user,
                { expiresIn: _default.ACCESS_TOKEN_TTL },
                "ACCESS_KEY"
            );

            return { accessToken, userData: user };
        } catch (error) {
            throw error;
        }
    }
}

export default JWTService;
