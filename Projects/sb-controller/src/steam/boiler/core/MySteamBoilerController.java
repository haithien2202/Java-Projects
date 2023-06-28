package steam.boiler.core;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import steam.boiler.model.SteamBoilerController;
import steam.boiler.util.Mailbox;
import steam.boiler.util.Mailbox.Message;
import steam.boiler.util.Mailbox.MessageKind;
import steam.boiler.util.SteamBoilerCharacteristics;


/**
 * @author Thien
 *
 */
public class MySteamBoilerController implements SteamBoilerController {

  /**
   * Captures the various modes in which the controller can operate.
   *
   * @author David J. Pearce
   *
   */
  private enum State {
    /**
     * This is state for waiting mode( init only).
     */
    WAITING,
    /**
     * this is state for ready mode ( when program finish init, start this).
     */
    READY,
    /**
     * this is state normal mode.
     */
    NORMAL,
    /**
     * this is state for degrade mode.
     */
    DEGRADED,
    /**
     * this is state for rescue mode.
     */
    RESCUE,
    /**
     * this is state fore emergency mode.
     */
    EMERGENCY_STOP
  }

  /**
   * Records the configuration characteristics for the given boiler problem.
   */
  private final SteamBoilerCharacteristics configuration;

  /**
   * Identifies the current mode in which the controller is operating.
   */
  private State mode = State.WAITING;
  /**
   * This contains mode of pump which will record the state of the pump based on our activities 
   * (note:for degrade mode check.) I just use boolean to indicate on-off for simplification.
   */
  private final boolean[] pumpMode;
  /**
   * This contains the message to open pump ( from 0 to pump num).
   */
  private final @NonNull Message[] pumpMessOp;
  /**
   * This contains the message to close pump( from 0 to pump num).
   */
  private final @NonNull Message[] pumpMessClo;
  /**
   * This contains the Message for pump failure dectection numbers from 0 to pump num.
   */
  private final @NonNull Message[] pumpFailure;
  /**
   * This contains the Message for pumpControlFailuredetection numbers from 0 to pump num.
   */
  private final @NonNull Message[] pumpControlFail;
  /**
   * This contains the Message fore pumpControlrepair acknowledgement numbers from 0 to pump num.
   */
  private final @NonNull Message[] pumpControlRepair;
  /**
   * This is Message for mode init which is used to send to to the system to start the mode.
   */
  private final Message modeInit = new Message(MessageKind.MODE_m, Mailbox.Mode.INITIALISATION);
  /**
   * This is Message for mode normal which is used to send to to the system to start the mode.
   */
  private final Message modeNormal = new Message(MessageKind.MODE_m, Mailbox.Mode.NORMAL);
  /**
   * This is Message for mode degrade which is used to send to to the system to start the mode.
   */
  private final Message modeDegrade = new Message(MessageKind.MODE_m, Mailbox.Mode.DEGRADED);
  /**
   * This is Message for mode rescue which is used to send to to the system to start the mode.
   */
  private final Message modeRescue = new Message(MessageKind.MODE_m, Mailbox.Mode.RESCUE);
  /**
   * This is Message for mode emergency which is used to send to to the system to start the mode.
   */
  private final Message modeEmer = new Message(MessageKind.MODE_m, Mailbox.Mode.EMERGENCY_STOP);
  /**
   * This is Message for the system to know its ready.
   */
  private final Message ready = new Message(MessageKind.PROGRAM_READY);
  /**
   * This is Message for the system to know it has steam failure.
   */
  private final Message steamFailure = new Message(MessageKind.STEAM_FAILURE_DETECTION);
  /**
   * This is Message for the system to know start Valve.
   */
  private final Message valve = new Message(MessageKind.VALVE);
  /**
   * This is Message for the system to know it has level failure.
   */
  private final Message levelFail = new Message(MessageKind.LEVEL_FAILURE_DETECTION);
  /**
   * This contains the Message for pump repair acknowledgement numbers from 0 to pump num.
   */
  private final @NonNull Message[] pumpRepair;
  /**
   * This is Message for the system to know we fix the level failure.
   */
  private final Message levelReac = new Message(MessageKind.LEVEL_REPAIRED_ACKNOWLEDGEMENT);
  /**
   * This is Message for the system to know we fix the steam failure.
   */
  private final Message steamReac = new Message(MessageKind.STEAM_REPAIRED_ACKNOWLEDGEMENT);

  /**
   * Construct a steam boiler controller for a given set of characteristics. The next is basically
   * just initialize all the array above with the size of array equals to number of used pumps. Then
   * add the corresponding message to it.
   *
   * @param configuration The boiler characteristics to be used.
   * 
   */

  public MySteamBoilerController(SteamBoilerCharacteristics configuration) {
    this.configuration = configuration;
    this.pumpMode = new boolean[this.configuration.getNumberOfPumps()];
    for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
      this.pumpMode[i] = false;
    }
    this.pumpMessOp = new @NonNull Message[this.configuration.getNumberOfPumps()];
    for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
      this.pumpMessOp[i] = new Message(MessageKind.OPEN_PUMP_n, i);
    }
    this.pumpMessClo = new @NonNull Message[this.configuration.getNumberOfPumps()];
    for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
      this.pumpMessClo[i] = new Message(MessageKind.CLOSE_PUMP_n, i);
    }
    this.pumpFailure = new @NonNull Message[this.configuration.getNumberOfPumps()];
    for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
      this.pumpFailure[i] = new Message(MessageKind.PUMP_FAILURE_DETECTION_n, i);
    }
    this.pumpControlFail = new @NonNull Message[this.configuration.getNumberOfPumps()];
    for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
      this.pumpControlFail[i] = new Message(MessageKind.PUMP_CONTROL_FAILURE_DETECTION_n, i);
    }
    this.pumpControlRepair = new @NonNull Message[this.configuration.getNumberOfPumps()];
    for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
      this.pumpControlRepair[i] =
          new Message(MessageKind.PUMP_CONTROL_REPAIRED_ACKNOWLEDGEMENT_n, i);
    }
    this.pumpRepair = new @NonNull Message[this.configuration.getNumberOfPumps()];
    for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
      this.pumpRepair[i] = new Message(MessageKind.PUMP_REPAIRED_ACKNOWLEDGEMENT_n, i);
    }
  }

  /**
   * This message is displayed in the simulation window, and enables a limited form of debug output.
   * The content of the message has no material effect on the system, and can be whatever is
   * desired. In principle, however, it should display a useful message indicating the current state
   * of the controller. if somethign is null or nonnull tag it above the methods
   *
   * @return Stringofthecurrentmode.
   * 
   */
  @Override
  @Nullable
  public String getStatusMessage() {
    return this.mode.toString();
  }


  /**
   * This is used to detect the normal mode, if we are in this mode, = true.
   */
  private boolean isNormalModeStart = false;
  /**
   * This is used to detect the degrade mode, if we are in this mode, = true.
   */
  private boolean isDegradeModeStart = false;
  /**
   * this is used to store the previous level, used for rescue mode.
   */
  private double levelMessageBefore = 0;
  /**
   * This is used to detect the rescue mode, if we are in this mode, = true.
   */
  private boolean isRecuseModeStart = false;
  /**
   * This is used to detect the start mode, if we are in this mode, = true.
   */
  private boolean isStart = true;

  /**
   * Process a clock signal which occurs every 5 seconds. This requires reading the set of incoming
   * messages from the physical units and producing a set of output messages which are sent back to
   * them.
   */

  @Override
  public void clock(@NonNull Mailbox incoming, @NonNull Mailbox outgoing) {
    // Extract expected messages
    @Nullable
    Message levelMessage = extractOnlyMatch(MessageKind.LEVEL_v, incoming);
    @Nullable
    Message steamMessage = extractOnlyMatch(MessageKind.STEAM_v, incoming);
    Message[] pumpStateMessages = extractAllMatches(MessageKind.PUMP_STATE_n_b, incoming);
    Message[] pumpControlStateMessages =
        extractAllMatches(MessageKind.PUMP_CONTROL_STATE_n_b, incoming);
    if (transmissionFailure(levelMessage, steamMessage, pumpStateMessages,
        pumpControlStateMessages)) {
      this.mode = State.EMERGENCY_STOP;
      outgoing.send(this.modeEmer);
    } else {
      assert levelMessage != null;
      assert steamMessage != null;
      modCheck(incoming, outgoing, steamMessage, levelMessage, 
          pumpStateMessages, pumpControlStateMessages);
      Message controlRepair = extractOnlyMatch(MessageKind.PUMP_CONTROL_REPAIRED_n, incoming);
      if (controlRepair != null) {
        outgoing.send(this.pumpControlRepair[controlRepair.getIntegerParameter()]);
        this.isNormalModeStart = true;
        this.isDegradeModeStart = false;
        this.isRecuseModeStart = false;
      }
      Message detectRepair = extractOnlyMatch(MessageKind.PUMP_REPAIRED_n, incoming);
      if (detectRepair != null) {
        outgoing.send(this.pumpRepair[detectRepair.getIntegerParameter()]);
        this.isNormalModeStart = true;
        this.isDegradeModeStart = false;
        this.isRecuseModeStart = false;
      }
      Message fixLevel = extractOnlyMatch(MessageKind.LEVEL_REPAIRED, incoming);
      if (fixLevel != null) {
        outgoing.send(this.levelReac);
        this.isNormalModeStart = true;
        this.isRecuseModeStart = false;
        this.isDegradeModeStart = false;
      }
      Message sensorRep = extractOnlyMatch(MessageKind.STEAM_REPAIRED, incoming);
      if (sensorRep != null) {
        outgoing.send(this.steamReac);
        this.isNormalModeStart = true;
        this.isRecuseModeStart = false;
        this.isDegradeModeStart = false;
      }
      stateStart(incoming, outgoing, steamMessage, levelMessage, pumpStateMessages);
    }
  }
  /**
   * This method is to check if any of the error or normal mode should happens
   * This will trigger the threshold which is isNormalModeStart,...
   * for state
   *
   * @param incoming incoming message
   * 
   * @param outgoing send message
   * 
   * @param steamMessage steam
   * 
   * @param levelMessage level
   * 
   * @param pumpStateMessages pumstate
   * 
   * @param pumpControlStateMessages pumpControlstate
   * 
   */
  
  public void modCheck(Mailbox incoming, Mailbox outgoing, Message steamMessage, 
      Message levelMessage, Message[] pumpStateMessages, Message[] pumpControlStateMessages) {
    
    double level = levelMessage.getDoubleParameter();
    double steam = steamMessage.getDoubleParameter();
    // if physical unit ready -> normalMode
    if (extractOnlyMatch(MessageKind.PHYSICAL_UNITS_READY, incoming) != null) {
      this.isNormalModeStart = true;
      this.isDegradeModeStart = false;
      this.isRecuseModeStart = false;
    }
    // if both sensor and level is broken at the same time -> emergency mode
    if (sensorErrCheck(steam) && levelErrCheck(level)) {
      this.mode = State.EMERGENCY_STOP;
      outgoing.send(this.modeEmer);
      return;
    }
    // if sensor is broken then send steam failure detection , activate degrade mode
    if (sensorErrCheck(steam)) {
      this.isNormalModeStart = false;
      this.mode = State.DEGRADED;
      this.isDegradeModeStart = true;
      this.isRecuseModeStart = false;
      outgoing.send(this.modeDegrade);
      outgoing.send(this.steamFailure);
    }
    // if level is broken then send level failure detection , activate recuse mode
    if (levelErrCheck(level)) {
      this.isNormalModeStart = false;
      this.isDegradeModeStart = false;
      this.isRecuseModeStart = true;
      this.mode = State.RESCUE;
      outgoing.send(this.modeRescue);
      outgoing.send(this.levelFail);
    }
    //if pump error -> activate degrademode
    if (pumpError(pumpStateMessages, outgoing)) {
      this.isNormalModeStart = false;
      this.mode = State.DEGRADED;
      this.isDegradeModeStart = true;
      this.isRecuseModeStart = false;
      outgoing.send(this.modeDegrade);
    }
    // if controller is broken -> activate degrademode
    if (isBroken(pumpStateMessages, pumpControlStateMessages, outgoing)) { 
      this.isNormalModeStart = false;
      this.mode = State.DEGRADED;
      this.isDegradeModeStart = true;
      this.isRecuseModeStart = false;
      outgoing.send(this.modeDegrade);
    }
  }
  
  /**
   * This moethod is to control the state of the pump
   * It will decide which mode to go and go into that mode.
   *
   * @param incoming
   * 
   * @param outgoing
   * 
   * @param steamMessage
   * 
   * @param levelMessage
   * 
   * @param pumpStateMessages 
   * 
   */
  public void stateStart(Mailbox incoming, Mailbox outgoing, Message steamMessage,
      Message levelMessage, Message[] pumpStateMessages) {
    Message pumpErr = extractOnlyMatch(MessageKind.PUMP_FAILURE_ACKNOWLEDGEMENT_n, incoming);
    Message senEr = extractOnlyMatch(MessageKind.STEAM_OUTCOME_FAILURE_ACKNOWLEDGEMENT, incoming);
    Message coEr = extractOnlyMatch(MessageKind.PUMP_CONTROL_FAILURE_ACKNOWLEDGEMENT_n, incoming);
    if (this.isStart) { // startMode
      initalMode(incoming, outgoing, steamMessage, levelMessage);
    } else if (this.isNormalModeStart) { //normalMode
      normalMode(levelMessage, pumpStateMessages, outgoing, steamMessage);
    } else if (this.isDegradeModeStart && senEr != null) { // degradeSensorMode
      degradeModeSensor(levelMessage, pumpStateMessages, outgoing);
    } else if (this.isDegradeModeStart && pumpErr != null) { //degradePumpErrMode
      int pumpFail = pumpErr.getIntegerParameter();
      degradeMode(levelMessage, pumpStateMessages, outgoing, steamMessage, pumpFail);
    } else if (this.isRecuseModeStart) { //RescueMode
      rescueMode(this.levelMessageBefore, pumpStateMessages, outgoing, steamMessage);
    } else if (this.isDegradeModeStart && coEr != null) {
      degradeMode2(levelMessage, outgoing, steamMessage);
    }
  }

  /**
   * this is used to detect pump error.
   * If mode is different from current state then return true.
   *
   * @param pumpStateMessages
   * 
   * @param outgoing
   * 
   * @return pumpErrorornot
   * 
   */
  public boolean pumpError(Message[] pumpStateMessages, Mailbox outgoing) {
    for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
      if (this.pumpMode[i] != pumpStateMessages[i].getBooleanParameter()) {
        outgoing.send(this.pumpFailure[i]); //detect error
        return true;
      }
    }
    return false;
  }

  /**
   * this is to check the open pump based on pumpMode.
   * (Used in degrade mode)
   *
   * @return NumberofopenPumpbaseonpumpMode
   * 
   */
  public int numberOfOpenedPump2() {
    int open = 0;
    for (int i = 0; i < this.pumpMode.length; i++) {
      if (this.pumpMode[i] == true) {
        open++;
      }
    }
    return open;
  }

  /**
   * This is degrade mode 2 which is used when a control is fail
   * The algor is relatively simple
   * because controller fails so we can't use pumpstatemessage, instead we use
   * pumpMode to check the behavior of the pump.
   * Check if level exceeds the warning level (limit level), yes then go emergency mode
   * This is algor 
   * + If the next level is lower than the normal level then we check how many pump  is open
   *    + we add 1 more and check if the water increased 
   *    ( yes -> change state of that pump in pumpmode->over)
   *        + if no  we add 1 more and do the thing above
   * + else if the next level is higher than normal level
   *    + we turn off 1 pump and see if the water goes down 
   *    ( yes -> change state of that pump in pumpmode->over)
   *        if no we turn off 1 more and do the thing above
   *   levelBfor = level to save levelBfor in case it change to rescue mode 
   *
   * @param levelMessage
   * 
   * @param outgoing 
   * 
   * @param steamMessage
   * 
   */
  public void degradeMode2(Message levelMessage, Mailbox outgoing, Message steamMessage) {
    if (upperUnderCheck(levelMessage.getDoubleParameter())) { // limit check
      this.mode = State.EMERGENCY_STOP;
      outgoing.send(this.modeEmer);
      return;
    }
    int numberOpenedPump = numberOfOpenedPump2();
    double steamOut = steamMessage.getDoubleParameter();
    double level = levelMessage.getDoubleParameter();
    double nextLevel = calExpectWater(level, numberOpenedPump, steamOut);
    if (nextLevel <= this.configuration.getMinimalNormalLevel() * 7 / 6) {
      for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
        //check if the water still increase
        if (steamOut > this.configuration.getPumpCapacity(0) * (i)) {
          if (this.pumpMode[i] == false) {
            outgoing.send(this.pumpMessOp[i]);
            this.pumpMode[i] = true;
          }
        }
      }
    } else if (nextLevel >= this.configuration.getMaximalNormalLevel() * 5 / 6) {
      int numberPumpOpen = numberOfOpenedPump2();
      for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
        //check if the water still increase
        if (steamOut < this.configuration.getPumpCapacity(0) * (numberPumpOpen)) {
          outgoing.send(this.pumpMessClo[numberPumpOpen - 1]);
          this.pumpMode[numberPumpOpen - 1] = false;
          numberPumpOpen--;
        }
      }
    }
    this.levelMessageBefore = level;
  }

  /**
   * This is normal mode  which is used when a the boiler is normal
   * The algor is relatively simple
   *  we  use pumpstatemessage check the behavior of the pump.
   * Check if level exceeds the warning level (limit level), yes then go emergency mode
   * This is algor 
   * + If the next level is lower than the normal level then we check how many pump  is open
   *    + we add 1 more and check if the water increased 
   *    ( yes -> change state of that pump in pumpmode->over)
   *        + if no  we add 1 more and do the thing above
   * + else if the next level is higher than normal level
   *    + we turn off 1 pump and see if the water goes down 
   *    ( yes -> change state of that pump in pumpmode->over)
   *        if no we turn off 1 more and do the thing above
   *   levelBfor = level to save levelBfor in case it change to rescue mode 
   *
   * @param levelMessage
   * 
   * @param pumpStateMessages
   * 
   * @param outgoing
   * 
   * @param steamMessage
   * 
   */
  public void normalMode(Message levelMessage, Message[] pumpStateMessages, Mailbox outgoing,
      Message steamMessage) {
    // in here we assume all things is working
    double level = levelMessage.getDoubleParameter();
    if (upperUnderCheck(level)) { // limit check
      this.mode = State.EMERGENCY_STOP;
      outgoing.send(this.modeEmer);
      return;
    }
    this.mode = State.NORMAL;
    outgoing.send(this.modeNormal);
    double steamOut = steamMessage.getDoubleParameter();
    int numberOpenedPump = numberOpenedPump(pumpStateMessages);
    double nextLevel = calExpectWater(level, numberOpenedPump, steamOut);
    if (nextLevel <= this.configuration.getMinimalNormalLevel() * 7 / 6) {
      for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
        if (steamOut > this.configuration.getPumpCapacity(0) * (i)) {
          if (!pumpStateMessages[i].getBooleanParameter()) {
            outgoing.send(this.pumpMessOp[i]);
            this.pumpMode[i] = true;
          }
        }
      }
    } else if (nextLevel >= this.configuration.getMaximalNormalLevel() * 5 / 6) {
      int numberPumpOpen = numberOpenedPump(pumpStateMessages);
      for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
        if (steamOut < this.configuration.getPumpCapacity(0) * (numberPumpOpen)) {
          outgoing.send(this.pumpMessClo[numberPumpOpen - 1]);
          this.pumpMode[numberPumpOpen - 1] = false;
          numberPumpOpen--;
        }
      }
    }
    this.levelMessageBefore = level;
  }

  /**
   * This is to check if the water exceeds the limit level.
   *
   * @param level
   * 
   * @return true if the level is higher or lesser than limit level
   * 
   */
  public boolean upperUnderCheck(double level) {
    double max = this.configuration.getMaximalLimitLevel();
    double min = this.configuration.getMinimalLimitLevel();
    return (level > max || level < min);

  }

  /**
   * This is Initial Mode.
   * If we receive message steam boiler waiting we can go to the init mode
   * check if steam != 0 -> emergency mode
   * check if level is error -> emegency mode
   * check if water is too high -> activate valve to release water
   *    + else if the water is too low -> open 4  pumps
   *    + else if the water between the desire level ->
   *        + deactivate start mode, send Message state that program is ready,close all pumps
   *    + save levelbefore = level every loop ( if its not <0) 
   *    
   * 
   *
   * @param incoming
   * 
   * @param outgoing
   * 
   * @param steamMessage
   * 
   * @param levelMessage
   * 
   */
  public void initalMode(Mailbox incoming, Mailbox outgoing, Message steamMessage,
      Message levelMessage) {
    outgoing.send(this.modeInit);
    double level = levelMessage.getDoubleParameter();
    double steam = steamMessage.getDoubleParameter();
    double maxNormal = this.configuration.getMaximalNormalLevel();
    double minNormal = this.configuration.getMinimalNormalLevel();
    if (extractOnlyMatch(MessageKind.STEAM_BOILER_WAITING, incoming) != null) {
      if (steam != 0) {
        outgoing.send(this.modeEmer);
        return;
      }
      if (levelErrCheck(level)) {
        this.mode = State.EMERGENCY_STOP;
        outgoing.send(this.modeEmer);
        return;
      }
      if (level > maxNormal) {
        outgoing.send(this.valve);
      } else if (level < minNormal) {
        for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
          outgoing.send(this.pumpMessOp[i]);
          this.pumpMode[i] = true;
        }
      } 
      
      if (level >= minNormal && level <= maxNormal) {
        this.isStart = false;
        for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
          outgoing.send(this.pumpMessClo[i]);
          this.pumpMode[i] = false;
        }
        this.mode = State.READY;
        outgoing.send(this.ready);
      }
      if (levelMessage.getDoubleParameter() > 0) {
        this.levelMessageBefore = levelMessage.getDoubleParameter();
      }
    }
  }


  /**
   * This is rescue mode which is used when level is fail
   * The algor is relatively simple
   * because level fails so we can't use levelMessage, instead we use
   * levelBefore  to predict the next level of the pump.
   * Check if levelbefore exceeds the warning level (limit level), yes then go emergency mode
   * This is algor 
   * + If the next level is lower than the normal level then we check how many pump  is open
   *    + we add 1 more and check if the water increased 
   *    ( yes -> change state of that pump in pumpmode->over)
   *        + if no  we add 1 more and do the thing above
   * + else if the next level is higher than normal level
   *    + we turn off 1 pump and see if the water goes down 
   *    ( yes -> change state of that pump in pumpmode->over)
   *        if no we turn off 1 more and do the thing above
   *
   * @param levelMessageBefore2 this is just levelBefore
   * 
   * @param pumpStateMessages
   * 
   * @param outgoing
   * 
   * @param steamMessage
   * 
   */

  private void rescueMode(double levelMessageBefore2, Message[] pumpStateMessages, Mailbox outgoing,
      Message steamMessage) {
    if (upperUnderCheck(levelMessageBefore2)) { // limit check
      this.mode = State.EMERGENCY_STOP;
      outgoing.send(this.modeEmer);
      return;
    }
    double steamOut = steamMessage.getDoubleParameter();
    int numberOpenedPump = numberOpenedPump(pumpStateMessages);
    double nextLevel = calExpectWater(levelMessageBefore2, numberOpenedPump, steamOut);
    
    if (nextLevel <= this.configuration.getMinimalNormalLevel() * 7 / 6) {
      for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
        if (steamOut > this.configuration.getPumpCapacity(0) * (i)) {
          if (!pumpStateMessages[i].getBooleanParameter()) {
            outgoing.send(this.pumpMessOp[i]);
            this.pumpMode[i] = true;
          }
        }
      }
    } else if (nextLevel >= this.configuration.getMaximalNormalLevel() * 5 / 6) {
      int numberPumpOpen = numberOpenedPump(pumpStateMessages);
      for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
        if (steamOut < this.configuration.getPumpCapacity(0) * (numberPumpOpen)) {
          if (numberPumpOpen > 0) {
          outgoing.send(this.pumpMessClo[numberPumpOpen - 1]);
          this.pumpMode[numberPumpOpen - 1] = false;
          numberPumpOpen--;
          }
        }

      }
    }
    this.levelMessageBefore = nextLevel;
  }

  /**
   * This is used to check if the steam sensor has some nonsense value.
   *
   * @param steamMessage
   * 
   * @return thesensorisErrorornot
   * 
   */
  public boolean sensorErrCheck(double steamMessage) {
    return steamMessage < 0 || steamMessage > this.configuration.getMaximualSteamRate();
  }

  /**
   * This is used to check if the level has some nonsense value.
   *
   * @param levelMessage
   * 
   * @return ifthelevelisError
   * 
   */
  public boolean levelErrCheck(double levelMessage) {
    return levelMessage < 0 || levelMessage >= this.configuration.getCapacity();
  }

  /**
   * idicate that there is a pump control message is wrong. 
   * By checking between pump control and pump state message.
   *
   * @param pumpStateMessages
   * 
   * @param pumpControlStateMessages
   * 
   * @param outgoing
   * 
   * @return isThereApControlbroken
   * 
   */
  public boolean isBroken(Message[] pumpStateMessages, Message[] pumpControlStateMessages,
      Mailbox outgoing) {
    for (int i = 0; i < pumpStateMessages.length; i++) {
      if (pumpStateMessages[i].getBooleanParameter() != pumpControlStateMessages[i]
          .getBooleanParameter()) {
        outgoing.send(this.pumpControlFail[i]);
        return true;
      }
    }
    return false;
  }

  /**
   *This is used to predict the nextLevel of water, this algorithm use
   *current water level, numberofopen pump and steamout to predict the next level
   *and return it.
   *
   * @param currentlevel 
   * 
   * @param numberOfOpenPump this is number of open pump
   * 
   * @param steamOut this is the steam out read from the boiler
   * 
   * @return levelnext
   */

  public double calExpectWater(double currentlevel, int numberOfOpenPump,
      double steamOut) {
    double pumpCap = this.configuration.getPumpCapacity(0);
    double levelNext = currentlevel + (5 * pumpCap * (numberOfOpenPump)) - steamOut * 5;
    return levelNext;
  }


  /**
   * This is degrade mode which is used when a pump is fail
   * The algor is relatively simple
   * because a pump is fail so we need is number which will be pumpFail.
   * Check if levelbefore exceeds the warning level (limit level), yes then go emergency mode
   * This is algor 
   * + If the next level is lower than the normal level then we check how many pump  is open
   *    + we add 1 more and check if the water increased 
   *        if the open pump is the broken one, we skip it, stack 1( means we need to open 1 more)
   *        (else yes -> change state of that pump in pumpmode->over)
   *        + if no  we add 1 more and do the thing above
   *    + if the stack is >0 -> find a close pump(not the broken) and then open it
   * + else if the next level is higher than normal level
   *    + we turn off 1 pump and see if the water goes down 
   *        if the close pump is the broken one, we skip it, stack 1( means we need to close 1 more)
   *        ( else and yes -> change state of that pump in pumpmode->over)
   *        if no we turn off 1 more and do the thing above
   *    + if the stack is >0 -> find a open pump(not the broken) and then close it.
   *
   * @param levelMessage
   * 
   * @param pumpStateMessages
   * 
   * @param outgoing
   * 
   * @param steamMessage
   * 
   * @param pumpFail
   * 
   */
  public void degradeMode(Message levelMessage, Message[] pumpStateMessages, Mailbox outgoing,
      Message steamMessage, int pumpFail) {
    if (upperUnderCheck(levelMessage.getDoubleParameter())) {
      this.mode = State.EMERGENCY_STOP;
      outgoing.send(this.modeEmer);
      return;
    }
    int numberOpenedPump = numberOpenedPump(pumpStateMessages);
    double nextLevel = calExpectWater(levelMessage.getDoubleParameter(),
        numberOpenedPump, steamMessage.getDoubleParameter());
    double steamOut = steamMessage.getDoubleParameter();
    int stack = 0;
    if (nextLevel <= this.configuration.getMinimalNormalLevel() * 7 / 6) {
      for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
        if (steamOut > this.configuration.getPumpCapacity(0) * (i)) {
          outgoing.send(this.pumpMessOp[i]);
          if (i == pumpFail) {
            stack = 1;
          } else {
            this.pumpMode[i] = true;
          }
        }
      }
      if (stack == 1) {
        for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
          if (this.pumpMode[i] == false && i != pumpFail) {
            outgoing.send(this.pumpMessOp[i]);
            this.pumpMode[i] = true;
            break;
          }
        }
      }
    } else if (nextLevel >= this.configuration.getMaximalNormalLevel() * 5 / 6) {
      int numberPumpOpen = numberOpenedPump(pumpStateMessages);
      for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
        if (steamOut < this.configuration.getPumpCapacity(0) * (numberPumpOpen)) {
          outgoing.send(this.pumpMessClo[numberPumpOpen - 1]);
          int current = numberPumpOpen - 1;
          if (current == pumpFail) {
            stack = 1;
          } else {
            this.pumpMode[numberPumpOpen - 1] = false;
          }
          numberPumpOpen--;
        }
      }
      if (stack == 1) {
        for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
          if (this.pumpMode[i] == true && i != pumpFail) {
            outgoing.send(this.pumpMessClo[i]);
            this.pumpMode[i] = false;
            break;
          }
        }
      }
    }
  }

  /**
   * This is degrade mode sensor  which is used when a steam sensor is fail
   * The algor is relatively simple
   * because sensor fails so we can't use steamMessage, instead we 
   * set a fix amount( closet to maximum) of steam out to check the behavior of the boiler.
   * Note : this is because if i use the maximum steam out. At the start, 
   * when steamout is very small, it will make the water go above the normal.
   * Check if level exceeds the warning level (limit level), yes then go emergency mode
   * This is algor 
   * + If the next level is lower than the normal level then we check how many pump  is open
   *    + we add 1 more and check if the water increased 
   *    ( yes -> change state of that pump in pumpmode->over)
   *        + if no  we add 1 more and do the thing above
   * + else if the next level is higher than normal level
   *    + we turn off 1 pump and see if the water goes down 
   *    ( yes -> change state of that pump in pumpmode->over)
   *        if no we turn off 1 more and do the thing above
   *
   * @param levelMessage
   * 
   * @param pumpStateMessages
   * 
   * @param outgoing
   * 
   */
  public void degradeModeSensor(Message levelMessage, Message[] pumpStateMessages,
      Mailbox outgoing) {
    int numberOpenedPump = numberOpenedPump(pumpStateMessages);
    double level = levelMessage.getDoubleParameter();
    if (upperUnderCheck(level)) { // limit check
      this.mode = State.EMERGENCY_STOP;
      outgoing.send(this.modeEmer);
      return;
    }
    double steamOut = this.configuration.getMaximualSteamRate() * 5 / 6;
    double nextLevel = calExpectWater(level, numberOpenedPump, steamOut);
    if (nextLevel <= this.configuration.getMinimalNormalLevel() * 7 / 6) {
      for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
        if (steamOut > this.configuration.getPumpCapacity(0) * (i)) {
          if (!pumpStateMessages[i].getBooleanParameter()) {
            outgoing.send(this.pumpMessOp[i]);
            this.pumpMode[i] = true;
          }
        }
      }
    } else if (nextLevel >= this.configuration.getMaximalNormalLevel() * 5 / 6) {
      int numberPumpOpen = numberOpenedPump(pumpStateMessages);
      for (int i = 0; i < this.configuration.getNumberOfPumps(); i++) {
        if (steamOut < this.configuration.getPumpCapacity(0) * (numberPumpOpen)) {
          outgoing.send(this.pumpMessClo[numberPumpOpen - 1]);
          this.pumpMode[numberPumpOpen - 1] = false;
          numberPumpOpen--;
        }

      }
    }
  }

  /**
   * This is for calculate the number of pump based on the pumpState message 
   * by analyze pumpStateMessage.
   *
   * @param m
   * 
   * @return numberOfOpenPump
   * 
   */
  public static int numberOpenedPump(Message[] m) {
    int numberOfOpenPump = 0;
    for (int i = 0; i < m.length; i++) {
      if (m[i].getBooleanParameter() == true) {
        numberOfOpenPump++;
      }
    }
    return numberOfOpenPump;
  }



  /**
   * Check whether there was a transmission failure. This is indicated in several ways. Firstly,
   * when one of the required messages is missing. Secondly, when the values returned in the
   * messages are nonsensical.
   *
   * @param levelMessage Extracted LEVEL_v message.
   * @param steamMessage Extracted STEAM_v message.
   * @param pumpStates Extracted PUMP_STATE_n_b messages.
   * @param pumpControlStates Extracted PUMP_CONTROL_STATE_n_b messages.
   * @return boolean
   */
  private boolean transmissionFailure(@Nullable Message levelMessage,
      @Nullable Message steamMessage, Message[] pumpStates, Message[] pumpControlStates) {
    // Check level readings
    if (levelMessage == null) {
      // Nonsense or missing level reading
      return true;
    } else if (steamMessage == null) {
      // Nonsense or missing steam reading
      return true;
    } else if (pumpStates.length != this.configuration.getNumberOfPumps()) {
      // Nonsense pump state readings
      return true;
    } else if (pumpControlStates.length != this.configuration.getNumberOfPumps()) {
      // Nonsense pump control state readings
      return true;
    }
    // Done
    return false;
  }

  /**
   * Find and extract a message of a given kind in a mailbox. This must the only match in the
   * mailbox, else <code>null</code> is returned.
   *
   * @param kind The kind of message to look for.
   * @param incoming The mailbox to search through.
   * @return The matching message, or <code>null</code> if there was not exactly one match.
   */
  private static @Nullable Message extractOnlyMatch(MessageKind kind, Mailbox incoming) {
    Message match = null;
    for (int i = 0; i != incoming.size(); ++i) {
      Message ith = incoming.read(i);
      if (ith.getKind() == kind) {
        if (match == null) {
          match = ith;
        } else {
          // This indicates that we matched more than one message of the given kind.
          return null;
        }
      }
    }
    return match;
  }

  /**
   * Find and extract all messages of a given kind.
   *
   * @param kind The kind of message to look for.
   * @param incoming The mailbox to search through.
   * @return The array of matches, which can empty if there were none.
   */
  private static Message[] extractAllMatches(MessageKind kind, Mailbox incoming) {
    int count = 0;
    // Count the number of matches
    for (int i = 0; i != incoming.size(); ++i) {
      Message ith = incoming.read(i);
      if (ith.getKind() == kind) {
        count = count + 1;
      }
    }
    // Now, construct resulting array
    Message[] matches = new Message[count];
    int index = 0;
    for (int i = 0; i != incoming.size(); ++i) {
      Message ith = incoming.read(i);
      if (ith.getKind() == kind) {
        matches[index++] = ith;
      }
    }
    return matches;
  }
}
