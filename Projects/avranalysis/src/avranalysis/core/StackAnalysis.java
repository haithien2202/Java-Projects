package avranalysis.core;

import java.util.HashMap;
import java.util.Map;
import javr.core.AvrDecoder;
import javr.core.AvrInstruction;
import javr.core.AvrInstruction.AbsoluteAddress;
import javr.core.AvrInstruction.RelativeAddress;
import javr.core.AvrInstruction.SBIS;
import javr.core.AvrInstruction.SBRS;
import javr.io.HexFile;
import javr.memory.ElasticByteMemory;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Responsible for determining the worst-case stack analysis for a given AVR
 * program.
 *
 * @author David J. Pearce
 *
 */
public class StackAnalysis {
  /**
  * Contains the raw bytes of the given firmware image being analysed.
  */
  private ElasticByteMemory firmware;

  /**
  * The decoder is used for actually decoding an instruction.
  */
  private AvrDecoder decoder = new AvrDecoder();

  /**
   * Records the maximum height seen so far.
   */
  private int maxHeight;

  /**
   * Records previous instructions and their height at the point.
  */
  @Nullable
  private Map<AvrInstruction.Opcode, Integer> save = new HashMap<>();
  
  /**
   *Construct a new analysis instance for a given hex file.
   *
   * @param hf Hexfile on which the analysis will be run.
   */
  public StackAnalysis(HexFile hf) {
    // Create firmware memory
    this.firmware = new ElasticByteMemory();
    // Upload image to firmware memory
    hf.uploadTo(this.firmware);
  }

  /**
   * Apply the stack analysis to the given firmware image producing a maximum
   * stack usage (in bytes).
   *
   * @return The maximum height observed thus far.
   */
  public int apply() {
    // Reset the maximum, height
    this.maxHeight = 0;
    // Traverse instructions starting at beginning
    traverse(0, 0);
    // Return the maximum height observed
    return this.maxHeight;
  }

  /**
  * Traverse the instruction at a given pc address, assuming the stack has a
  * given height on entry.
  *
  * @param pc            Program Counter of instruction to traverse
  * @param currentHeight Current height of the stack at this point (in bytes)
  */
  private void traverse(int pc, int currentHeight) {
    // Check whether current stack height is maximum
    this.maxHeight = Math.max(this.maxHeight, currentHeight);

    // Check whether we have terminated or not
    if ((pc * 2) >= this.firmware.size()) {
      // We've gone over end of instruction sequence, so stop.
      return;
    }
    // Process instruction at this address
    AvrInstruction instruction = decodeInstructionAt(pc);
    // Move to the next logical instruction as this is always the starting point.
    int next = pc + instruction.getWidth();
    //
    process(instruction, next, currentHeight);
  }

  /**
  * Process the effect of a given instruction.
  *
  * @param instruction   Instruction to process
  * @param pc            Program counter of following instruction
  * @param currentHeight Current height of the stack at this point (in bytes)
  */
  private void process(AvrInstruction instruction, int pc, int currentHeight) {
    // Check if map is null
    assert (save != null);
    // If the instruction was previously visited
    if (save.containsKey(instruction.getOpcode())) { 
      //If the height is the same, stop the loop
      if (currentHeight == save.get(instruction.getOpcode())) {
        return;
      }
      // If the height is bigger than the previous visited height
      // and height is larger than 400, stop the loop and make maxHeight = max value
      if (currentHeight > save.get(instruction.getOpcode()) && currentHeight >= 400) {
        this.maxHeight = Integer.MAX_VALUE;
        return;
      }
    }
    switch (instruction.getOpcode()) {
      case BREQ: {
        RelativeAddress branch = (RelativeAddress) instruction;
        // Save the current opcode and the current height
        save.put(instruction.getOpcode(), currentHeight);
        //Traverse through 2 branches
        traverse(pc + branch.k, currentHeight);
        traverse(pc, currentHeight);
        break;
      }
      case BRGE: {
        RelativeAddress branch = (RelativeAddress) instruction;
        // Save the current opcode and the current height
        save.put(instruction.getOpcode(), currentHeight);
        // Traverse through 2 branches
        traverse(pc + branch.k, currentHeight);
        traverse(pc, currentHeight);
        break;
      }
      case BRLT: {
        RelativeAddress branch = (RelativeAddress) instruction;
        // Save the current opcode and the current height
        save.put(instruction.getOpcode(), currentHeight);
        //Traverse through 2 branches
        traverse(pc + branch.k, currentHeight);
        traverse(pc, currentHeight);
        break;
      }
      case SBRS: {
        SBRS branch = (SBRS) instruction;
        // Save the current opcode and the current height
        save.put(instruction.getOpcode(), currentHeight);
        //Traverse through 2 branches
        traverse(pc + branch.getWidth(), currentHeight);
        traverse(pc, currentHeight);
        break;
      }
      case CALL: {
        AbsoluteAddress branch = (AbsoluteAddress) instruction;
        // Check whether infinite loop; if so, terminate.
        if (branch.k != -1) {
          // Traverse through 2 branches
          traverse(branch.k, currentHeight + 2);
          traverse(pc, currentHeight);
        }
        break;
      }
      case RCALL: {
        RelativeAddress branch = (RelativeAddress) instruction;
        // Explore the branch target
        // Check whether infinite loop; if so, terminate.
        if (branch.k != -1) {
          // Save the current opcode and the current height
          save.put(instruction.getOpcode(), currentHeight);
          traverse(pc + branch.k, currentHeight + 2);
          traverse(pc, currentHeight);
        }
        break;
      }
      case JMP: {
        // Explore the branch target
        traverse(pc, currentHeight - 1);
        break;
      }
      case RJMP: {
        // NOTE: this one is implemented for you.
        RelativeAddress branch = (RelativeAddress) instruction;
        // Check whether infinite loop; if so, terminate.
        if (branch.k != -1) {
          // Explore the branch target
          traverse(pc + branch.k, currentHeight);
        }
        //
        break;
      }
      case RET:
        //Stop the loop
        break;
      case RETI:
        //Stop the loop  
        break;
      case PUSH: {
        //Add 1 from the height
        traverse(pc, currentHeight + 1); 
        break;
      }
      case POP: {
        //Minus 1 from the height
        traverse(pc, currentHeight - 1);
        break;
      }
      case SBIS: {
        // Save the current opcode and the current height
        save.put(instruction.getOpcode(), currentHeight);
        SBIS branch = (SBIS) instruction;
        // Explore the branch target
        //Traverse through 2 branches
        traverse(pc + branch.getWidth(), currentHeight);
        traverse(pc, currentHeight);
        break;
      }
      case BRLO: {
        // Save the current opcode and the current height
        save.put(instruction.getOpcode(), currentHeight);
        // Explore the branch target
        //Traverse through 2 branches
        RelativeAddress branch = (RelativeAddress) instruction;
        traverse(pc + branch.getWidth(), currentHeight);
        traverse(pc, currentHeight);
        break;
      }
      case LDS: {
        // Explore the branch target
        traverse(pc + 1, currentHeight);
        break;
      }
      case BRSH: {
        // Save the current opcode and the current height
        save.put(instruction.getOpcode(), currentHeight);
        RelativeAddress branch = (RelativeAddress) instruction;
        // Explore the branch target
        //Traverse through 2 branches
        traverse(pc + branch.getWidth(), currentHeight);
        traverse(pc, currentHeight);
        break;
      }
      default:
        // Indicates a standard instruction where control is transferred to the
        // following instruction.
        traverse(pc, currentHeight);
    }
  }

  /**
  * Decode the instruction at a given PC location.
  *
  * @param pc Address of instruction to decode.
  * @return Instruction which has been decoded.
  */
  private AvrInstruction decodeInstructionAt(int pc) {
    AvrInstruction insn = this.decoder.decode(this.firmware, pc);
    assert insn != null;
    return insn;
  }
}