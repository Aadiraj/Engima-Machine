package enigma;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import static enigma.TestUtils.*;

public class MachineTest {

    @Test
    public void testDoubleStep() {
        Alphabet ac = new CharacterRange('A', 'C');
        Rotor one = new Reflector("R1", new Permutation("", ac));
        Rotor two = new MovingRotor("R2", new Permutation("", ac), "C");
        Rotor three = new MovingRotor("R3", new Permutation("", ac), "C");
        Rotor four = new MovingRotor("R4", new Permutation("", ac), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotors = {"R1", "R2", "R3", "R4"};
        Machine mach = new Machine(ac, 4, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AACA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABAB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABAC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABBA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABBB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABBC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABCA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACAB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACAC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACBA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACBB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACBC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACCA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAB", getSetting(ac, machineRotors));
    }

    @Test
    public void testDoubleStep2() {
        Alphabet ac = new CharacterRange('A', 'D');
        Rotor one = new Reflector("R1", new Permutation("(AC) (BD)", ac));
        Rotor two = new MovingRotor("R2", new Permutation("(ABCD)", ac), "C");
        Rotor three = new MovingRotor("R3", new Permutation("(ABCD)", ac), "C");
        Rotor four = new MovingRotor("R4", new Permutation("(ABCD)", ac), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotors = {"R1", "R2", "R3", "R4"};
        Machine mach = new Machine(ac, 4, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAB", getSetting(ac, machineRotors));
        mach.convert('a');
    }

    /**
     * Helper method to get the String
     * representation of the current Rotor settings
     */
    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }

    @Test
    public void myTest() {
        Alphabet ac = new CharacterRange('A', 'Z');
        Rotor one = new Reflector("R1", new Permutation(
                "(AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV) "
                        + "(LM) (PW) (QZ) (SX) (UY)", ac));
        Rotor two = new FixedRotor("R2",
                new Permutation("(AFNIRLBSQWVXGUZDKMTPCOYJHE)", ac));
        Rotor three = new MovingRotor("R3",
                new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", ac), "V");
        Rotor four = new MovingRotor("R4",
                new Permutation("(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)", ac), "Z");
        Rotor five = new MovingRotor("R5",
                new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) "
                        + "(BJ) (GR) (NT) (A) (Q)", ac), "E");
        String setting = "BBBB";
        Permutation plugboard = new Permutation("(AZ) (PY) (LN)", ac);
        Rotor[] machineRotors = {one, two, three, four, five};
        String[] rotors = {"R1", "R2", "R3", "R4", "R5"};
        Machine mach = new Machine(ac, 5, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);
        mach.setPlugboard(plugboard);
        assertEquals("IJKGGTLVTQIDNWZWVBFSFDHFMTTJIXJXHRWPCYGBYNPUKESK"
                        + "NHZWNCBTSIAKFNYPAMANXKOSDAVJCELSOFTXZZMSRBYOMYDRZYE",
                mach.convert("There once was a boy named Harry destined to "
                        + "be a star His parents where killed by Voldemort "
                        + "who gave him a lightning scar"));
    }
}
