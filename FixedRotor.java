package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Aadiraj Batlaw
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        _name = name;
        _perm = perm;
    }


    /** Rotor name. */
    private String _name;

    /** Rotor permutation.*/
    private Permutation _perm;

}
