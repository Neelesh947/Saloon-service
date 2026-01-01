export interface DecodedTokenDTO {
    exp: number;                   // Expiration timestamp (in seconds)
    iat: number;                   // Issued at timestamp (in seconds)
    jti: string;                   // Token ID
    iss: string;                    // Issuer
    aud: string;                    // Audience
    sub: string;                    // Subject / User ID
    typ: string;                    // Token type (e.g., "Bearer")
    azp?: string;                   // Authorized party (application)
    sid?: string;                    // Session ID
    scope?: string;                  // Scope / roles
    "allowed-origins"?: string[];   // Allowed origins
    realm_access?: {
        roles: string[];
    };
    resource_access?: {
        [resource: string]: {
            roles: string[];
        };
    };
    email_verified?: boolean;
    name?: string;
    preferred_username?: string;
    given_name?: string;
    family_name?: string;
    email?: string;
    address?: string;
    phoneNumber?: string;
}