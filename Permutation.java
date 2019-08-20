package enigma;

import static enigma.EnigmaException.*;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 * @author Aadiraj Batlaw
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(String cycle) {
        _cycles += cycle;
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return _alphabet.size();
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        char letter = _alphabet.toChar(wrap(p));
        char nextLetter = letter;
        for (int i = 0; i < _cycles.length() - 1; i++) {
            if (letter == _cycles.charAt(i)) {
                if (_cycles.charAt(i + 1) == ')') {
                    for (int j = i; j > -1; j--) {
                        if (_cycles.charAt(j) == '(') {
                            nextLetter = _cycles.charAt(j + 1);
                            break;
                        }
                    }
                } else {
                    nextLetter = _cycles.charAt(i + 1);
                }
                break;
            }
        }
        return _alphabet.toInt(nextLetter);
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        char letter = _alphabet.toChar(wrap(c));
        char nextLetter = letter;
        for (int i = 1; i < _cycles.length(); i++) {
            if (letter == _cycles.charAt(i)) {
                if (_cycles.charAt(i - 1) == '(') {
                    for (int j = i; j < _cycles.length(); j++) {
                        if (_cycles.charAt(j) == ')') {
                            nextLetter = _cycles.charAt(j - 1);
                            break;
                        }
                    }
                } else {
                    nextLetter = _cycles.charAt(i - 1);
                }
                break;
            }
        }
        return _alphabet.toInt(nextLetter);
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        int index = _alphabet.toInt(p);
        int result = permute(index);
        return _alphabet.toChar(result);
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    char invert(char c) {
        int index = _alphabet.toInt(c);
        return _alphabet.toChar(invert(index));
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (permute(i) == _alphabet.toChar(i)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation.*/
    private Alphabet _alphabet;

    /** String of cycles. */
    private String _cycles;

}
