package enigma;

import java.util.Collection;

/**
 * Class that represents a complete enigma machine.
 *
 * @author Aadiraj Batlaw
 */
class Machine {

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;

    /** Machine pawls. */
    private int _pawls;

    /** All machine rotors. */
    private Collection<Rotor> _allRotors;

    /** All machine rotors slots. */
    private Rotor[] _rotorSlots;

    /** Machine plugboard. */
    private Permutation _plugboard;

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _pawls = pawls;
        _allRotors = allRotors;
        _rotorSlots = new Rotor[numRotors];
        _plugboard = new Permutation("", alpha);

    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _rotorSlots.length;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor rotor : _allRotors) {
                if (rotor.name().toUpperCase().equals(rotors[i])) {
                    _rotorSlots[i] = rotor;
                    break;
                }
            }
        }
    }

    /** @return rotorSlots. */
    public Rotor[] getRotorSlots() {
        return _rotorSlots;
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 upper-case letters. The first letter refers to the
     * leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        for (int i = 0; i < _rotorSlots.length - 1; i++) {
            _rotorSlots[i + 1].set(_alphabet.toInt(setting.charAt(i)));
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * <p>
     * the machine.
     */
    int convert(int c) {
        int j;
        for (int i = 2; i < _rotorSlots.length - 1; i++) {
            if (_rotorSlots[i].atNotch()) {
                _rotorSlots[i - 1].advance();
                _rotorSlots[i].advance();
                j = i;
                while (j + 1 > _rotorSlots.length - 1
                        && _rotorSlots[j + 1].atNotch()) {
                    _rotorSlots[j + 1].advance();
                    j++;
                }
                i = j;
            }
        }
        if (_rotorSlots[_rotorSlots.length - 1].atNotch()) {
            _rotorSlots[_rotorSlots.length - 2].advance();
        }
        _rotorSlots[_rotorSlots.length - 1].advance();
        int result = _plugboard.permute(c);
        for (int x = _rotorSlots.length - 1; x >= 0; x--) {
            result = _rotorSlots[x].convertForward(result);
        }
        for (int y = 1; y < _rotorSlots.length; y++) {
            result = _rotorSlots[y].convertBackward(result);
        }
        return _plugboard.permute(result);
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        String message = msg.replace(" ", "").toUpperCase();
        char[] decoded = new char[message.length()];
        for (int i = 0; i < message.length(); i++) {
            decoded[i] = _alphabet.toChar(
                    convert(_alphabet.toInt(message.charAt(i))));
        }
        String result = new String(decoded);
        return result;
    }
}
