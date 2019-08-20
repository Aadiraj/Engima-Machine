package enigma;

import static enigma.EnigmaException.*;

/**
 * Class that represents a rotating rotor in the enigma machine.
 *
 * @author Aadiraj Batlaw
 */
class MovingRotor extends Rotor {

    /**
     * A rotor named NAME whose permutation in its default setting is
     * PERM, and whose notches are at the positions indicated in NOTCHES.
     * The Rotor is initally in its 0 setting (first character of its
     * alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _name = name;
        _notches = notches;
        _perm = perm;
    }


    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            if (_perm.alphabet().toChar(_perm.wrap(this.setting()))
                    == _notches.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        set(_perm.wrap(setting() + 1));
    }

    /**
     * Name of rotor.
     */
    private String _name;

    /**
     * Notches.
     */
    private String _notches;

    /**
     * Rotor permutation.
     */
    private Permutation _perm;

}
