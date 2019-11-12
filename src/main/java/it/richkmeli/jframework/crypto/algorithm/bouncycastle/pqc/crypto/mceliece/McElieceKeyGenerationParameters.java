package it.richkmeli.jframework.crypto.algorithm.bouncycastle.pqc.crypto.mceliece;

import it.richkmeli.jframework.crypto.algorithm.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

public class McElieceKeyGenerationParameters
        extends KeyGenerationParameters {
    private McElieceParameters params;

    public McElieceKeyGenerationParameters(
            SecureRandom random,
            McElieceParameters params) {
        // XXX key size?
        super(random, 256);
        this.params = params;
    }

    public McElieceParameters getParameters() {
        return params;
    }
}
