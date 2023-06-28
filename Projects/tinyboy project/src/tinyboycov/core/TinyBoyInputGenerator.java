package tinyboycov.core;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import org.eclipse.jdt.annotation.Nullable;
import tinyboy.core.ControlPad;
import tinyboy.core.TinyBoyInputSequence;
import tinyboy.util.AutomatedTester;

/**
 * The TinyBoy Input Generator is responsible for generating and refining inputs
 * to try and ensure that sufficient branch coverage is obtained.
 *
 * @author David J. Pearce
 *
 */
public class TinyBoyInputGenerator implements AutomatedTester.InputGenerator<TinyBoyInputSequence> {
  /**
  * Represents the number of buttons on the control pad.
  */
  private static final int NUM_BUTTONS = ControlPad.Button.values().length;
  
  /**
  * Current batch being processed.
  */
  private ArrayList<TinyBoyInputSequence> worklist = new ArrayList<>();

  /**
   * Create new input generator for the TinyBoy simulation.
   * This method generates the pulses array input for the automatic test
   * It generates an array of 500 pulses for 50 times
   * It then shuffles the worklist array to make it even more random
   * The pulse depends on a math random function to generate 
   * a random number which determine the input button
   * 
   */
  public TinyBoyInputGenerator() {
    for (int a = 0; a < 50; a++) {
      ControlPad.Button[] pulses = new ControlPad.Button[500];
      for (int i = 0; i < 500; i++) {
        int r = (int) (Math.random() * NUM_BUTTONS);
        switch (r) {
          default:
            pulses[i] = ControlPad.Button.RIGHT;
            break;  
          case 0:
            pulses[i] = ControlPad.Button.DOWN;
            break;
          case 1:
            pulses[i] = ControlPad.Button.UP;
            break;
          case 2:
            pulses[i] = ControlPad.Button.LEFT;
            break;
          case 3:
            pulses[i] = ControlPad.Button.RIGHT;
            break;
        }
      }
      this.worklist.add(new TinyBoyInputSequence(pulses));
    }
    randomSample(worklist, worklist.size());
  }

  @Override
  public boolean hasMore() {
    return this.worklist.size() > 0;
  }

  @Override
  public @Nullable TinyBoyInputSequence generate() {
    if (!this.worklist.isEmpty()) {
      // remove last item from worklist
      return this.worklist.remove(this.worklist.size() - 1);
    }
    return null;
  }

  /**
   * A record returned from the fuzzer indicating the coverage and final state
   * obtained for a given input sequence.
   * It prints out the result from the test
   * 
   */
  @Override
  public void record(TinyBoyInputSequence input, BitSet coverage, byte[] state) {
    // NOTE: this method is called when fuzzing has finished for a given input. It
    // produces three potentially useful items: firstly, the input sequence that was
    // used for fuzzing; second, the set of instructions which were covered when
    // executing that sequence; finally, the complete state of the machine's RAM at
    // the end of the run.
    //
    // At this point, you will want to use the feedback gained from fuzzing to help
    // prune the space of inputs to try next. A few helper methods are given below,
    // but you will need to write a lot more.
    System.out.println(input);
    for (int i = 0; i < coverage.length(); i++) {
      System.out.print(coverage.get(i) + " ");
    }
    System.out.println("");
    System.out.println(state);
  }

  /**
  * Check whether a given input sequence is completely subsumed by another.
  *
  * @param lhs The one which may be subsumed.
  * @param rhs The one which may be subsuming.
  * @return True if lhs subsumed by rhs, false otherwise.
  */
  public static boolean subsumedBy(BitSet lhs, BitSet rhs) {
    for (int i = lhs.nextSetBit(0); i >= 0; i = lhs.nextSetBit(i + 1)) {
      if (!rhs.get(i)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Reduce a given set of items to at most <code>n</code> inputs by randomly
   * sampling.
   *
   * @param inputs List of inputs to sample from.
   * @param n      Size of inputs after reduction.
   */
  private static <T> void randomSample(List<T> inputs, int n) {
    // Randomly shuffle inputs
    Collections.shuffle(inputs);
    // Remove inputs until only n remain
    while (inputs.size() > n) {
      inputs.remove(inputs.size() - 1);
    }
  }
}
