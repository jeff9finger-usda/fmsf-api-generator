# OpenAPI Generator for the default library

## Overview
The intent of the project is to provide generated SDK artifacts from the FMSF v2 OpenAPI document, that can be used to integrate API consumer with this service.
The OpenAPI generator has many options and the SDKs generated here were selected for our immediate clients, but you may use the structure to generated your own SDK according to your needs.

### Generated Client SDKs:
- Java using Apache HTTP Client
- Java using Spring WebClient
- Java using Jersey 3 and Java HTTP client (TODO)
- Postman Collection (does not currently get built by default)
- TypeScript-Angular (TODO)
- TypeScript-Node (TODO)

### Generated Server Artifacts:
- Java using SpringBoot Server (Not Tested)

### PGP Keys for GNUPG (gpg)
In order to publish the generated artifacts to `maven.central`, a PGP key must be generated. The key generation only needs to be
performed once. But I have documented the steps here. Execute the following command, and answer the questions. Use the default
options for the kind of key, its size, and the expiration (validation period).

// TODO remove me -- old key: `01CE11D2BE3710BA2285EF46C7117191650983F1`
The key-id for the current key is: `8D5F22440EFF8BCC54586998307803155C7256C8`

`gpg --full-generate-key`
- What kind of key? -> 1 (default: RSA and RSA)
- What keysize? -> 2048 (default)
- Valid for? 0 (default: does not expire)
- Real name: FMSF API
- Email address: OCIO-EAS-FMSF-Admins@usda.gov
- Comment: PGP/GPG Signature for FMSF Artifacts

You will then be prompted for a pass phrase.

It has been distributed to the `keys.openpgp.org` key server. The following command was used to perform that action:
`gpg --keyserver keys.openpgp.org --send-keys 8D5F22440EFF8BCC54586998307803155C7256C8`

In addition, the key-id will be shown. This will be used to export the public key and the private key for use in the CI/CD.

Export the public key: `gpg --export -a 8D5F22440EFF8BCC54586998307803155C7256C8 > fmsf-public-key.asc`
Export the private/secret key: `gpg ---export-secret-keys -a 8D5F22440EFF8BCC54586998307803155C7256C8 > fmsf-private-key.asc`

Create a revoke key to revoke the key in the future if needed and the secret-key is lost: `gpg --gen-revoke 8D5F22440EFF8BCC54586998307803155C7256C8 --output fmsf-revoke-key.asc`
This requires the passphrase.

The secret key must be installed into the CI/CD system performing the build as it is required by `gpg` to sign artifacts.
The command used to perform this action is: `gpg --import fmsf-private-key.asc`

### Using PGP with Maven
When building this project, two the environment variables should be set to allow proper signing of the artifacts that will
be published to `maven.central`. This is a secure way to provide this data in the AWS CI/CD environment - these values can be stored
in *AWS SecretsManager* and passed securely to the environment variables.
- `MAVEN_GPG_KEY`: Contains the GPG signing key id for signing. This vale can be specified in the pom, but if that is not desired, this process can be used. Example: `MAVEN_GPG_KEY=thekeymaterial`
- `MAVEN_GPG_PASSPHRASE`: Contains the GPG passphrase corresponding to the signing key id. Example:  `MAVEN_GPG_PASSPHRASE=thephrase`.

### Build Notes
Four values are required to allow the generated artifacts to be published to `maven.central`.

Three of these are for the PGP signature used by `maven-gpg-plugin`, which in turn uses `gpg`. PGP requires a public key and a secret or private key (See above for how those are generated).
The public key has been uploaded to the OpenPGP ket server `keys.openpgp.org`, but the secret key is required to sign the artifact. And this is only one of the three items.
* The secret key must be imported into the build operating systems gnupg configuration
* The GPG `keyname` (AKA `key-id` above) is required in order for the `gpg` command to access the secret key. This is stored in `maven-settings.xml`, which allows the `maven-gpg-plugin` to reference its value.
* The PGP passphrase is also required to access the secret key. The `MAVEN_GPG_PASSPHRASE` must be set as shell environment variable to enable digital signing of the artifacts.

The fourth value is required to publish the signed artifact to `maven.central`, and requires a user token and key as well. Currently, these are stored unencrypted in `maven-settings.xml` in this project.
There is an option to encrypt the passwords in the file using maven's encryption process, but not sure if that is required. At this point
TODO - See if maven encryption is necessary.

With these ideas in mind, the following commands must be executed on order for the maven build to be successful.
```shell
# If the user has an existing GPG local key repository (probably only required on local developer systems),
# and you don't want to pollute it, use the following:
# export GNUPGHOME=$(mktemp -d)

gpg --batch --import <pnupg-secret-key-file> # fmsf-private-key.asc from above
MAVEN_GPG_PASSPHRASE='<gnupg-secret-key-passphrase>' mvn install
```

------
##### What's OpenAPI
The goal of OpenAPI is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection.
When properly described with OpenAPI, a consumer can understand and interact with the remote service with a minimal amount of implementation logic.
Similar to what interfaces have done for lower-level programming, OpenAPI removes the guesswork in calling the service.

Check out [OpenAPI-Spec](https://github.com/OAI/OpenAPI-Specification) for additional information about the OpenAPI project, including additional libraries with support for other languages and more.

