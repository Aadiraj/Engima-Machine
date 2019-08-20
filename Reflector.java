package enigma;

import static enigma.EnigmaException.*;

/**
 * Class that represents a reflector in the enigma.
 *
 * @author Aadiraj Batlaw
 */
class Reflector extends FixedRotor {

    /**
     * A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM.
     */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        _name = name;
        _perm = perm;
        _setting = 0;
    }


    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    /**
     * Name of rotor.
     */
    private String _name;

    /**
     * Rotor permutation.
     */
    private Permutation _perm;

    /**
     * Rotor setting.
     */
    private int _setting;

}
