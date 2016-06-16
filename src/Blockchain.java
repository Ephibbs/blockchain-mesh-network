/*
* Blockchain.java
* An implementation of blockchain
 */

public class Blockchain {
    public Blockchain() {

    }

    Block solvePuzzle(Block block, int difficulty) {
        if (difficulty < 1) {
            difficulty = 1;
        } else if (difficulty > 32) {
            difficulty = 32;
        }

        boolean verified = false;
        String blockStr = block.toString();

        while (!verified) {
            // Get hash
            nonce = new BigInteger(130, new SecureRandom()).toString(32);
            int hc = (blockStr + nonce).hashCode();
            String hash = String.format("%32s", Integer.toBinaryString(hc)).replace(" ", "0");

            // Verified?
            for (int i = 0; i < hash.length(); i++) {
                if (hash.charAt(i) == '1') {
                    break;
                } else if (i == difficulty-1) {
                    verified = true;
                    break;
                }
            }

            if (verified) {
                block.setNonce(nonce);
            }
        }
        return block;  
    }
}
