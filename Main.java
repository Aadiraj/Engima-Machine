package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static enigma.EnigmaException.error;

/**
 * Enigma simulator.
 *
 * @author Aadiraj Batlaw
 */
public final class Main {

    /**
     * Alphabet used in this machine.
     */
    private Alphabet _alphabet;
    /**
     * Source of input messages.
     */
    private Scanner _input;
    /**
     * Source of machine configuration.
     */
    private Scanner _config;
    /**
     * File for encoded/decoded messages.
     */
    private PrintStream _output;


    /**
     * Check ARGS and open the necessary files (see comment on main).
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }


    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() {
        Machine machine = readConfig();
        Pattern pat = Pattern.compile("\\*");
        Pattern pat2 = Pattern.compile(".");
        if (!_input.hasNext(pat)) {
            throw error("input does not begin with setting");
        }
        while (_input.hasNextLine()) {
            while (_input.hasNext(pat)) {
                _input.next();
                String[] rotors = new String[machine.numRotors()];
                int i = 0;
                while (i < rotors.length) {
                    rotors[i] = _input.next();
                    i++;
                }
                for (int j = 0; j < rotors.length; j++) {
                    for (int k = 0; k < rotors.length; k++) {
                        if (rotors[j] == rotors[k] && j != k) {
                            throw error("Repeated rotors");
                        }
                    }
                }
                try {
                    machine.insertRotors(rotors);
                } catch (NoSuchElementException e) {
                    throw error("Rotors misnamed");
                }
                if (!machine.getRotorSlots()[0].reflecting()) {
                    throw error("First rotor is not a reflector");
                }
                String settings = _input.next();
                String plugCycles = _input.nextLine();
                Permutation plug = new Permutation(plugCycles, _alphabet);

                if (settings.length() != machine.numRotors() - 1) {
                    throw error("wrong number of arguments");
                }
                for (int j = 0; j < settings.length(); j++) {
                    if (!_alphabet.contains(settings.charAt(j))) {
                        throw error("characters not contained in the alphabet");
                    }
                }
                machine.setPlugboard(plug);
                setUp(machine, settings);
            }
            if (!_input.hasNext()) {
                break;
            } else {
                String test = _input.nextLine();
                printMessageLine(machine.convert(test));
                if (!test.isEmpty()) {
                    _output.println();
                }
                if (_input.hasNext(pat2)) {
                    _output.println();
                }
            }
        }
    }

    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        try {

            String alpha = _config.next();
            int numRotors = Integer.parseInt(_config.next());
            int numPawls = Integer.parseInt(_config.next());
            _config.nextLine();
            _alphabet = new CharacterRange(alpha.charAt(0),
                    alpha.charAt(alpha.length() - 1));
            Collection<Rotor> allRotors = new ArrayList<>();
            while (_config.hasNextLine()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /**
     * Return a rotor, reading its description from _config.
     */
    private Rotor readRotor() {
        try {
            Rotor rotor;
            String name = _config.next();
            String type = _config.next();
            String notches = type.substring(1);
            String cycles = _config.nextLine();
            Pattern pat = Pattern.compile("\\(\\w+\\)");
            while (_config.hasNext(pat)) {
                cycles += _config.nextLine();
                if (!_config.hasNext()) {
                    break;
                }
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            if (type.charAt(0) == 'M') {
                rotor = new MovingRotor(name, perm, notches);
            } else if (type.charAt(0) == 'N') {
                rotor = new FixedRotor(name, perm);
            } else {
                rotor = new Reflector(name, perm);
            }
            return rotor;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }


    }

    /**
     * Set M according to the specification given on SETTINGS,
     * which must have the format specified in the assignment.
     */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /**
     * Print MSG in groups of five (except that the last group may
     * have fewer letters).
     */
    private void printMessageLine(String msg) {
        int i = 0;
        while (i < msg.length() - 5) {
            _output.append(msg.substring(i, i + 5) + " ");
            i += 5;
        }
        _output.append(msg.substring(i));

    }
}
