# SunBangjie
###### \java\seedu\ptman\commons\encrypter\EncrypterTest.java
``` java
public class EncrypterTest {

    @Test
    public void encrypt_validString_returnsCorrectEncryptedString() throws Exception {
        String plainText = "Alex Yeoh";
        String expectedCipherText = "CurU9CCNY0mueTcpOaMg/w==";
        String cipherText = encrypt(plainText);
        assertEquals(cipherText, expectedCipherText);
    }

    @Test
    public void decrypt_validString_returnsCorrectDecryptedString() throws Exception {
        String expectedPlainText = "Alex Yeoh";
        String cipherText = "CurU9CCNY0mueTcpOaMg/w==";
        String plainText = decrypt(cipherText);
        assertEquals(plainText, expectedPlainText);
    }
}
```
###### \java\seedu\ptman\logic\commands\AnnouncementCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code AnnouncementCommand}.
 */
public class AnnouncementCommandTest {
    private Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());

    @Test
    public void equals() {
        Announcement firstAnnouncement = new Announcement("First Announcement.");
        Announcement secondAnnouncement = new Announcement("Second Announcement.");
        AnnouncementCommand firstAnnouncementCommand = new AnnouncementCommand(firstAnnouncement);
        AnnouncementCommand secondAnnouncementCommand = new AnnouncementCommand(secondAnnouncement);

        // same object -> return true
        assertTrue(firstAnnouncementCommand.equals(firstAnnouncementCommand));

        // same values -> return true
        AnnouncementCommand firstAnnouncementCommandCopy = new AnnouncementCommand(firstAnnouncement);
        assertTrue(firstAnnouncementCommand.equals(firstAnnouncementCommandCopy));

        // different types -> return false
        assertFalse(firstAnnouncementCommand.equals(1));

        // null -> return false
        assertFalse(firstAnnouncementCommand.equals(null));

        // different announcement -> return false
        assertFalse(firstAnnouncementCommand.equals(secondAnnouncementCommand));
    }

    @Test
    public void execute_nonAdminMode_failure() {
        AnnouncementCommand command = prepareCommand("New Announcement!");
        assertCommandFailure(command, model, MESSAGE_ACCESS_DENIED);
    }

    @Test
    public void execute_adminModeValidAnnouncement_commandFailed() {
        model.setTrueAdminMode(new Password());
        String expectedMessage = AnnouncementCommand.MESSAGE_EDIT_OUTLET_SUCCESS;
        AnnouncementCommand command = prepareCommand("Valid Announcement");
        assertCommandSuccess(command, model, expectedMessage, model);
    }

    /**
     * Parses {@code userInput} into a {@code AnnouncementCommand}
     */
    private AnnouncementCommand prepareCommand(String userInput) {
        AnnouncementCommand command = new AnnouncementCommand(new Announcement(userInput));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\ptman\logic\commands\DecryptDataCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code DecryptDataCommand}.
 */
public class DecryptDataCommandTest {

    private Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());

    @Test
    public void execute_nonAdminMode_failure() {
        DecryptDataCommand command = prepareCommand();
        assertCommandFailure(command, model, MESSAGE_ACCESS_DENIED);
    }

    @Test
    public void execute_adminModeDataEncrypted_success() {
        model.setTrueAdminMode(new Password());
        model.encryptLocalStorage();
        DecryptDataCommand command = prepareCommand();
        Model expectedModel = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());
        expectedModel.decryptLocalStorage();
        String expectedMessage = DecryptDataCommand.MESSAGE_DECRYPT_SUCCESS;
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_adminModeDataAlreadyEncrypted_failure() {
        model.setTrueAdminMode(new Password());
        DecryptDataCommand command = prepareCommand();
        String expectedMessage = DecryptDataCommand.MESSAGE_DECRYPT_FAILURE;
        assertCommandFailure(command, model, expectedMessage);
    }

    /**
     * Returns an {@code EncryptDataCommand}
     */
    private DecryptDataCommand prepareCommand() {
        DecryptDataCommand command = new DecryptDataCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\ptman\logic\commands\EditOutletCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code EditOutletCommand}.
 */
public class EditOutletCommandTest {

    private Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());

    @Test
    public void execute_nonAdminMode_failure() {
        OutletName outletName = new OutletName("EditedOutlet");
        OperatingHours operatingHours = new OperatingHours("1000-1800");
        OutletContact outletContact = new OutletContact("912345678");
        OutletEmail outletEmail = new OutletEmail("EditedOutlet@gmail.com");
        EditOutletCommand command = prepareCommand(outletName, operatingHours, outletContact, outletEmail);
        assertCommandFailure(command, model, MESSAGE_ACCESS_DENIED);
    }

    @Test
    public void execute_adminModeAllFieldsValid_success() {
        model.setTrueAdminMode(new Password());
        OutletName outletName = new OutletName("EditedOutlet");
        OperatingHours operatingHours = new OperatingHours("1000-1800");
        OutletContact outletContact = new OutletContact("912345678");
        OutletEmail outletEmail = new OutletEmail("EditedOutlet@gmail.com");
        EditOutletCommand command = prepareCommand(outletName, operatingHours, outletContact, outletEmail);
        String expectedMessage = EditOutletCommand.MESSAGE_EDIT_OUTLET_SUCCESS;
        OutletInformation expectedOutlet = new OutletInformation();
        try {
            expectedOutlet.setOutletInformation(outletName, operatingHours, outletContact, outletEmail);
        } catch (NoOutletInformationFieldChangeException e) {
            fail("This should not fail because all outlet information fields are specified.");
        }
        Model expectedModel = new ModelManager(new PartTimeManager(model.getPartTimeManager()), new UserPrefs(),
                expectedOutlet);
        try {
            expectedModel.updateOutlet(expectedOutlet);
        } catch (NoOutletInformationFieldChangeException e) {
            fail("This should not fail because all outlet information fields are specified.");
        }
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_adminModeNonFieldsSpecified_failure() {
        model.setTrueAdminMode(new Password());
        EditOutletCommand command = prepareCommand(null, null, null, null);
        String expectedMessage = EditOutletCommand.MESSAGE_EDIT_OUTLET_FAILURE;
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_nonAdminModeNonFieldSpecified_failure() {
        EditOutletCommand command = prepareCommand(null, null, null, null);
        assertCommandFailure(command, model, MESSAGE_ACCESS_DENIED);
    }

    @Test
    public void executeUndoRedo_adminModeAllFieldsValid_success() throws Exception {
        model.setTrueAdminMode(new Password());
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        OutletName outletName = new OutletName("EditedOutlet");
        OperatingHours operatingHours = new OperatingHours("1000-1800");
        OutletContact outletContact = new OutletContact("912345678");
        OutletEmail outletEmail = new OutletEmail("EditedOutlet@gmail.com");
        EditOutletCommand command = prepareCommand(outletName, operatingHours, outletContact, outletEmail);
        OutletInformation expectedOutlet = new OutletInformation();
        try {
            expectedOutlet.setOutletInformation(outletName, operatingHours, outletContact, outletEmail);
        } catch (NoOutletInformationFieldChangeException e) {
            fail("This should not fail because all outlet information fields are specified.");
        }
        Model expectedModel = new ModelManager(new PartTimeManager(model.getPartTimeManager()), new UserPrefs(),
                new OutletInformation());
        // edit -> outlet edited
        command.execute();
        undoRedoStack.push(command);

        // undo -> reverts parttimemanager back to previous state
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first employee edited again
        try {
            expectedModel.updateOutlet(expectedOutlet);
        } catch (NoOutletInformationFieldChangeException e) {
            fail("This should not fail because all outlet information fields are specified.");
        }
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        OutletName outletName = new OutletName("EditedOutlet");
        OperatingHours operatingHours = new OperatingHours("1000-1800");
        OutletContact outletContact = new OutletContact("912345678");
        OutletEmail outletEmail = new OutletEmail("EditedOutlet@gmail.com");
        final EditOutletCommand standardCommand = prepareCommand(outletName, operatingHours,
                outletContact, outletEmail);

        // same values -> returns true
        OutletName sameName = new OutletName("EditedOutlet");
        OperatingHours sameOperatingHours = new OperatingHours("1000-1800");
        OutletContact sameOutletContact = new OutletContact("912345678");
        OutletEmail sameOutletEmail = new OutletEmail("EditedOutlet@gmail.com");
        EditOutletCommand commandWithSameValues = prepareCommand(sameName, sameOperatingHours,
                sameOutletContact, sameOutletEmail);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        OutletName differentName = new OutletName("different");
        OperatingHours differentOperatingHours = new OperatingHours("0900-1000");
        OutletContact differentOutletContact = new OutletContact("123456789");
        OutletEmail differentOutletEmail = new OutletEmail("different@gmail.com");

        // different outlet name -> returns false
        assertFalse(standardCommand.equals(new EditOutletCommand(differentName, operatingHours,
                outletContact, outletEmail)));

        // different operating hours -> returns false
        assertFalse(standardCommand.equals(new EditOutletCommand(outletName, differentOperatingHours,
                outletContact, outletEmail)));

        // different outlet contact -> returns false
        assertFalse(standardCommand.equals(new EditOutletCommand(outletName, operatingHours,
                differentOutletContact, outletEmail)));

        // different outlet contact -> returns false
        assertFalse(standardCommand.equals(new EditOutletCommand(outletName, operatingHours,
                outletContact, differentOutletEmail)));
    }

    /**
     * Returns an {@code EditOutletCommand}
     */
    private EditOutletCommand prepareCommand(OutletName outletName, OperatingHours operatingHours,
                                             OutletContact outletContact, OutletEmail outletEmail) {
        EditOutletCommand editOutletCommand = new EditOutletCommand(outletName, operatingHours,
                outletContact, outletEmail);
        editOutletCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editOutletCommand;
    }
}
```
###### \java\seedu\ptman\logic\commands\EncryptDataCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code EncryptDataCommand}.
 */
public class EncryptDataCommandTest {

    private Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());

    @Test
    public void execute_nonAdminMode_failure() {
        EncryptDataCommand command = prepareCommand();
        assertCommandFailure(command, model, MESSAGE_ACCESS_DENIED);
    }

    @Test
    public void execute_adminModeDataNotEncrypted_success() {
        model.setTrueAdminMode(new Password());
        EncryptDataCommand command = prepareCommand();
        Model expectedModel = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());
        expectedModel.encryptLocalStorage();
        String expectedMessage = EncryptDataCommand.MESSAGE_ENCRYPT_SUCCESS;
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_adminModeDataAlreadyEncrypted_failure() {
        model.setTrueAdminMode(new Password());
        model.encryptLocalStorage();
        EncryptDataCommand command = prepareCommand();
        String expectedMessage = EncryptDataCommand.MESSAGE_ENCRYPT_FAILURE;
        assertCommandFailure(command, model, expectedMessage);
    }

    /**
     * Returns an {@code EncryptDataCommand}
     */
    private EncryptDataCommand prepareCommand() {
        EncryptDataCommand command = new EncryptDataCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\ptman\logic\commands\ViewEncryptionCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code ViewOutletCommand}.
 */
public class ViewEncryptionCommandTest {
    private ViewEncryptionCommand command;
    private Model model;
    private Model expectedModel;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(),
                new OutletInformation());
        expectedModel = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(),
                new OutletInformation());
        command = new ViewEncryptionCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_validOutletInformation_showsCorrectInformation() {
        String expectedMessage = "Local storage files are not encrypted.";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }
}
```
###### \java\seedu\ptman\logic\parser\AnnouncementCommandParserTest.java
``` java
public class AnnouncementCommandParserTest {

    private AnnouncementCommandParser parser = new AnnouncementCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", AnnouncementCommand.MESSAGE_EDIT_OUTLET_FAILURE);
    }

    @Test
    public void parse_validArgs_returnsAnnouncementCommand() {
        AnnouncementCommand expectedCommand = new AnnouncementCommand(new Announcement("new announcement"));
        assertParseSuccess(parser, "new announcement", expectedCommand);
    }
}
```
###### \java\seedu\ptman\logic\parser\EditOutletCommandParserTest.java
``` java
public class EditOutletCommandParserTest {

    private EditOutletCommandParser parser = new EditOutletCommandParser();

    @Test
    public void parse_invalidValue_failure() {
        String commandInvalidName = EditOutletCommand.COMMAND_WORD + INVALID_OUTLET_NAME_DESC;
        assertParseFailure(parser, commandInvalidName, OutletName.MESSAGE_NAME_CONSTRAINTS);

        String commandInvalidOperatingHours = EditOutletCommand.COMMAND_WORD + INVALID_OPERATING_HOURS_DESC;
        assertParseFailure(parser, commandInvalidOperatingHours,
                OperatingHours.MESSAGE_OPERATING_HOUR_CONSTRAINTS);

        String commandInvalidOutletContact = EditOutletCommand.COMMAND_WORD + INVALID_OUTLET_CONTACT_DESC;
        assertParseFailure(parser, commandInvalidOutletContact, OutletContact.MESSAGE_OUTLET_CONTACT_CONSTRAINTS);

        String commandInvalidOutletEmail = EditOutletCommand.COMMAND_WORD + INVALID_OUTLET_EMAIL_DESC;
        assertParseFailure(parser, commandInvalidOutletEmail, OutletEmail.MESSAGE_OUTLET_EMAIL_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsValid_success() {
        String userInput = EditOutletCommand.COMMAND_WORD
                + OUTLET_NAME_DESC + OPERATING_HOURS_DESC + OUTLET_CONTACT_DESC + OUTLET_EMAIL_DESC;
        OutletName outletName = new OutletName(VALID_OUTLET_NAME);
        OperatingHours operatingHours = new OperatingHours(VALID_OPERATING_HOURS);
        OutletContact outletContact = new OutletContact(VALID_OUTLET_CONTACT);
        OutletEmail outletEmail = new OutletEmail(VALID_OUTLET_EMAIL);
        EditOutletCommand expectedCommand = new EditOutletCommand(outletName, operatingHours,
                outletContact, outletEmail);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
```
###### \java\seedu\ptman\logic\parser\ParserUtilTest.java
``` java
    @Test
    public void parseOutletName_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOutletName((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOutletName((Optional<String>) null));
    }

    @Test
    public void parseOutletName_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOutletName(INVALID_OUTLET_NAME));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOutletName(
                Optional.of(INVALID_OUTLET_NAME)));
    }

    @Test
    public void parseOutletName_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseOutletName(Optional.empty()).isPresent());
    }

    @Test
    public void parseOutletName_validValueWithoutWhitespace_returnsOutletName() throws Exception {
        OutletName expectedName = new OutletName(VALID_OUTLET_NAME);
        assertEquals(expectedName, ParserUtil.parseOutletName(VALID_OUTLET_NAME));
        assertEquals(Optional.of(expectedName), ParserUtil.parseOutletName(Optional.of(VALID_OUTLET_NAME)));
    }

    @Test
    public void parseOutletName_validValueWithWhitespace_returnsTrimmedOutletName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_OUTLET_NAME + WHITESPACE;
        OutletName expectedName = new OutletName(VALID_OUTLET_NAME);
        assertEquals(expectedName, ParserUtil.parseOutletName(nameWithWhitespace));
        assertEquals(Optional.of(expectedName), ParserUtil.parseOutletName(Optional.of(nameWithWhitespace)));
    }

    @Test
    public void parseOutletContact_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOutletContact((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOutletContact((Optional<String>) null));
    }

    @Test
    public void parseOutletContact_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOutletContact(
                INVALID_OUTLET_CONTACT));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOutletContact(
                Optional.of(INVALID_OUTLET_CONTACT)));
    }

    @Test
    public void parseOutletContact_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseOutletContact(Optional.empty()).isPresent());
    }

    @Test
    public void parseOutletContact_validValueWithoutWhitespace_returnsOutletContact() throws Exception {
        OutletContact expectedOutletContact = new OutletContact(VALID_OUTLET_CONTACT);
        assertEquals(expectedOutletContact, ParserUtil.parseOutletContact(VALID_OUTLET_CONTACT));
        assertEquals(Optional.of(expectedOutletContact), ParserUtil.parseOutletContact(
                Optional.of(VALID_OUTLET_CONTACT)));
    }

    @Test
    public void parseOutletContact_validValueWithWhitespace_returnsTrimmedOutletContact() throws Exception {
        String outletContactWithWhitespace = WHITESPACE + VALID_OUTLET_CONTACT + WHITESPACE;
        OutletContact expectedOutletContact = new OutletContact(VALID_OUTLET_CONTACT);
        assertEquals(expectedOutletContact, ParserUtil.parseOutletContact(outletContactWithWhitespace));
        assertEquals(Optional.of(expectedOutletContact), ParserUtil.parseOutletContact(
                Optional.of(outletContactWithWhitespace)));
    }

    @Test
    public void parseOperatingHours_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOperatingHours((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOperatingHours((Optional<String>) null));
    }

    @Test
    public void parseOperatingHours_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOperatingHours(
                INVALID_OPERATING_HOURS));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOperatingHours(
                Optional.of(INVALID_OPERATING_HOURS)));
    }

    @Test
    public void parseOperatingHours_invalidStartEndTimeOrder_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOperatingHours(
                INVALID_START_END_TIME));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOperatingHours(
                Optional.of(INVALID_START_END_TIME)));
    }

    @Test
    public void parseOperatingHours_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseOperatingHours(Optional.empty()).isPresent());
    }

    @Test
    public void parseOperatingHours_validValueWithoutWhitespace_returnsOperatingHours() throws Exception {
        OperatingHours expectedOperatingHours = new OperatingHours(VALID_OPERATING_HOURS);
        assertEquals(expectedOperatingHours, ParserUtil.parseOperatingHours(VALID_OPERATING_HOURS));
        assertEquals(Optional.of(expectedOperatingHours), ParserUtil.parseOperatingHours(
                Optional.of(VALID_OPERATING_HOURS)));
    }

    @Test
    public void parseOperatingHours_validValueWithWhitespace_returnsTrimmedOperatingHours() throws Exception {
        String operatingHoursWithWhitespace = WHITESPACE + VALID_OPERATING_HOURS + WHITESPACE;
        OperatingHours expectedOperatingHours = new OperatingHours(VALID_OPERATING_HOURS);
        assertEquals(expectedOperatingHours, ParserUtil.parseOperatingHours(operatingHoursWithWhitespace));
        assertEquals(Optional.of(expectedOperatingHours), ParserUtil.parseOperatingHours(
                Optional.of(operatingHoursWithWhitespace)));
    }
```
###### \java\seedu\ptman\logic\parser\PartTimeManagerParserTest.java
``` java
    @Test
    public void parseCommand_editoutlet() throws Exception {
        String name = "EditedOutlet";
        String operatingHours = "1000-1700";
        String outletContact = "91234567";
        String outletEmail = "EditedOutlet@gmail.com";
        EditOutletCommand command = (EditOutletCommand) parser.parseCommand(EditOutletCommand.COMMAND_WORD
                + " " + PREFIX_OUTLET_NAME + name
                + " " + PREFIX_OPERATING_HOURS + operatingHours
                + " " + PREFIX_OUTLET_CONTACT + outletContact
                + " " + PREFIX_OUTLET_EMAIL + outletEmail);
        assertEquals(new EditOutletCommand(new OutletName(name), new OperatingHours(operatingHours),
                new OutletContact(outletContact), new OutletEmail(outletEmail)), command);
    }

    @Test
    public void parseCommand_editoutletAlias() throws Exception {
        String name = "EditedOutlet";
        String operatingHours = "1000-1700";
        String outletContact = "91234567";
        String outletEmail = "EditedOutlet@gmail.com";
        EditOutletCommand command = (EditOutletCommand) parser.parseCommand(EditOutletCommand.COMMAND_ALIAS
                + " " + PREFIX_OUTLET_NAME + name
                + " " + PREFIX_OPERATING_HOURS + operatingHours
                + " " + PREFIX_OUTLET_CONTACT + outletContact
                + " " + PREFIX_OUTLET_EMAIL + outletEmail);
        assertEquals(new EditOutletCommand(new OutletName(name), new OperatingHours(operatingHours),
                new OutletContact(outletContact), new OutletEmail(outletEmail)), command);
    }

    @Test
    public void parseCommand_announcement() throws Exception {
        String announcement = "new announcement.";
        AnnouncementCommand command = (AnnouncementCommand) parser.parseCommand(AnnouncementCommand.COMMAND_WORD
                + " " + announcement);
        assertEquals(new AnnouncementCommand(new Announcement(announcement)), command);
    }

    @Test
    public void parseCommand_announcementAlias() throws Exception {
        String announcement = "new announcement.";
        AnnouncementCommand command = (AnnouncementCommand) parser.parseCommand(AnnouncementCommand.COMMAND_ALIAS
                + " " + announcement);
        assertEquals(new AnnouncementCommand(new Announcement(announcement)), command);
    }

    @Test
    public void parseCommand_encrypt() throws Exception {
        assertTrue(parser.parseCommand(EncryptDataCommand.COMMAND_WORD) instanceof EncryptDataCommand);
    }

    @Test
    public void parseCommand_decrypt() throws Exception {
        assertTrue(parser.parseCommand(DecryptDataCommand.COMMAND_WORD) instanceof DecryptDataCommand);
    }

    @Test
    public void parseCommand_viewoutlet() throws Exception {
        assertTrue(parser.parseCommand(ViewEncryptionCommand.COMMAND_WORD) instanceof ViewEncryptionCommand);
    }

    @Test
    public void parseCommand_viewoutletAlias() throws Exception {
        assertTrue(parser.parseCommand(ViewEncryptionCommand.COMMAND_ALIAS) instanceof ViewEncryptionCommand);
    }

```
###### \java\seedu\ptman\model\ModelManagerTest.java
``` java
    @Test
    public void updateOutlet_validCondition_success() throws NoOutletInformationFieldChangeException {
        PartTimeManager partTimeManager = new PartTimeManager();
        PartTimeManager differentPartTimeManager = new PartTimeManager();
        UserPrefs userPrefs = new UserPrefs();

        OutletInformation outlet = new OutletInformation(new OutletName("test"), new OperatingHours("1000-1500"),
                new OutletContact("123"), new OutletEmail("test@test.com"),
                new Announcement("New Announcement."), new Password(), false);

        ModelManager modelManager = new ModelManager(differentPartTimeManager, userPrefs, new OutletInformation());
        ModelManager differentModelManager = new ModelManager(partTimeManager, userPrefs, new OutletInformation());

        assertEquals(modelManager, differentModelManager);
        modelManager.updateOutlet(outlet);
        assertNotEquals(modelManager, differentModelManager);
    }

    @Test
    public void getEncryptionMode_defaultData_returnsFalse() {
        ModelManager modelManager = new ModelManager();
        assertFalse(modelManager.getEncryptionMode());
    }

    @Test
    public void getEncryptionModeMessage_defaultData_returnsCorrectMessage() {
        ModelManager modelManager = new ModelManager();
        String actualMessage = modelManager.getEncryptionModeMessage();
        String expectedMessage = new OutletInformation().getEncryptionModeMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void getEncryptionMode_encryptedData_returnsTrue() {
        ModelManager modelManager = new ModelManager();
        modelManager.encryptLocalStorage();
        assertTrue(modelManager.getEncryptionMode());
    }

    @Test
    public void getEncryptionModeMessage_encryptedData_returnsCorrectMessage() {
        ModelManager modelManager = new ModelManager();
        modelManager.encryptLocalStorage();
        String actualMessage = modelManager.getEncryptionModeMessage();
        OutletInformation outletInformation = new OutletInformation();
        outletInformation.setEncryptionMode(true);
        String expectedMessage = outletInformation.getEncryptionModeMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void encryptLocalStorage_dataNotEncrypted_success() {
        PartTimeManager partTimeManager = new PartTimeManager();
        PartTimeManager differentPartTimeManager = new PartTimeManager();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(partTimeManager, userPrefs, new OutletInformation());
        ModelManager differentModelManager = new ModelManager(differentPartTimeManager, userPrefs,
                new OutletInformation());

        assertEquals(modelManager, differentModelManager);
        modelManager.encryptLocalStorage();
        assertNotEquals(modelManager, differentModelManager);
    }

    @Test
    public void decryptLocalStorage_dataEncrypted_success() {
        PartTimeManager partTimeManager = new PartTimeManager();
        PartTimeManager differentPartTimeManager = new PartTimeManager();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(partTimeManager, userPrefs, new OutletInformation());
        ModelManager differentModelManager = new ModelManager(differentPartTimeManager, userPrefs,
                new OutletInformation());

        modelManager.encryptLocalStorage();
        differentModelManager.encryptLocalStorage();
        assertEquals(modelManager, differentModelManager);
        modelManager.decryptLocalStorage();
        assertNotEquals(modelManager, differentModelManager);
    }
```
###### \java\seedu\ptman\model\outlet\NameTest.java
``` java
public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new OutletName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new OutletName(invalidName));
    }

    @Test
    public void isValidName_nullName_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> OutletName.isValidName(null));
    }

    @Test
    public void isValidName_invalidName_returnsFalse() {
        assertFalse(OutletName.isValidName("")); // empty string
        assertFalse(OutletName.isValidName(" ")); // spaces only
        assertFalse(OutletName.isValidName("^")); // only non-alphanumeric characters
        assertFalse(OutletName.isValidName("coffee*")); // contains non-alphanumeric characters
    }

    @Test
    public void isValidName_validName_returnsTrue() {
        assertTrue(OutletName.isValidName("cool coffee")); // alphabets only
        assertTrue(OutletName.isValidName("12345")); // numbers only
        assertTrue(OutletName.isValidName("cool coffee 3rd branch")); // alphanumeric characters
        assertTrue(OutletName.isValidName("Cool Coffee")); // with capital letters
        assertTrue(OutletName.isValidName("The Best and Coolest Coffee in the World")); // long names
    }

    @Test
    public void toString_sameValue_returnsTrue() {
        OutletName test = new OutletName("Cool Coffee");
        assertEquals(test.toString(), "Cool Coffee");
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        OutletName test = new OutletName("Cool Coffee");
        OutletName other = new OutletName("Cool Coffee");
        assertTrue(test.equals(other));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        OutletName test = new OutletName("Cool Coffee");
        OutletName other = new OutletName("Coolest Coffee");
        assertFalse(test.equals(other));
    }

    @Test
    public void hashCode_sameFullName_returnsTrue() {
        OutletName test = new OutletName("Cool Coffee");
        assertEquals(test.hashCode(), "Cool Coffee".hashCode());
    }
}
```
###### \java\seedu\ptman\model\outlet\OperatingHoursTest.java
``` java
public class OperatingHoursTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new OperatingHours(null));
    }

    @Test
    public void constructor_invalidOperatingHours_throwsIllegalArgumentException() {
        String invalidOperatingHours = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new OperatingHours(invalidOperatingHours));
    }

    @Test
    public void isValidOperatingHours_nullOperatingHours_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> OperatingHours.isValidOperatingHours(null));
    }

    @Test
    public void isValidOperatingHours_blankOperatingHours_returnsFalse() {
        assertFalse(OperatingHours.isValidOperatingHours("")); // empty string
        assertFalse(OperatingHours.isValidOperatingHours(" ")); // spaces only
    }

    @Test
    public void isValidOperatingHours_missingParts_returnsFalse() {
        assertFalse(OperatingHours.isValidOperatingHours("-22:00")); // missing start time
        assertFalse(OperatingHours.isValidOperatingHours("09:00-")); // missing end time
        assertFalse(OperatingHours.isValidOperatingHours("-")); // missing both start and end time
    }

    @Test
    public void isValidOperatingHours_invalidParts_returnsFalse() {
        assertFalse(OperatingHours.isValidOperatingHours("2500-2200")); // invalid hour in start time
        assertFalse(OperatingHours.isValidOperatingHours("09000-2200")); // more than two numbers of hour
        assertFalse(OperatingHours.isValidOperatingHours("0999-2200")); // invalid minute in start time
        assertFalse(OperatingHours.isValidOperatingHours("09000-2200")); // more than two numbers of minute
        assertFalse(OperatingHours.isValidOperatingHours("0900-2500")); // invalid hour in end time
        assertFalse(OperatingHours.isValidOperatingHours("0900-22000")); // more than two numbers of hour
        assertFalse(OperatingHours.isValidOperatingHours("0900-2299")); // invalid minute in end time
        assertFalse(OperatingHours.isValidOperatingHours("0900-22000")); // more than two numbers of minute
        assertFalse(OperatingHours.isValidOperatingHours("09.00-2200")); // invalid '.' symbol used in start
        assertFalse(OperatingHours.isValidOperatingHours("09/00-2200")); // invalid '/' symbol used in start
        assertFalse(OperatingHours.isValidOperatingHours("09@00-2200")); // invalid '@' symbol used in start
        assertFalse(OperatingHours.isValidOperatingHours("0900-22.00")); // invalid '.' symbol used in end
        assertFalse(OperatingHours.isValidOperatingHours("0900-22/00")); // invalid '/' symbol used in end
        assertFalse(OperatingHours.isValidOperatingHours("0900-22@00")); // invalid '@' symbol used in end
        assertFalse(OperatingHours.isValidOperatingHours(" 0900-2200")); // leading space
        assertFalse(OperatingHours.isValidOperatingHours("0900-2200 ")); // trailing space
        assertFalse(OperatingHours.isValidOperatingHours("0900--2200")); // double '-' symbol
        assertFalse(OperatingHours.isValidOperatingHours("0900/2200")); // invalid '/' symbol used to connect
        assertFalse(OperatingHours.isValidOperatingHours("0900.2200")); // invalid '.' symbol used to connect
        assertFalse(OperatingHours.isValidOperatingHours("0900@2200")); // invalid '@' symbol used to connect
    }

    @Test
    public void isValidOperatingHours_validOperatingHours_returnsTrue() {
        assertTrue(OperatingHours.isValidOperatingHours("0900-2200"));
        assertTrue(OperatingHours.isValidOperatingHours("1000-2100"));
        assertTrue(OperatingHours.isValidOperatingHours("0800-1800"));
    }

    @Test
    public void isValidStartTimeEndTimeOrder_invalidOrder_returnsFalse() {
        assertFalse(OperatingHours.isValidStartTimeEndTimeOrder("2200-1000"));
        assertFalse(OperatingHours.isValidStartTimeEndTimeOrder("1000-1000"));
    }

    @Test
    public void isValidStartTimeEndTimeOrder_validOrder_returnsTrue() {
        assertTrue(OperatingHours.isValidStartTimeEndTimeOrder("1200-2000"));
        assertTrue(OperatingHours.isValidStartTimeEndTimeOrder("1200-1230"));
    }

    @Test
    public void convertStringToLocalTime_validInput_returnsLocalTime() {
        LocalTime localTime = LocalTime.of(9, 0);
        String test = "0900";
        assertEquals(OperatingHours.convertStringToLocalTime(test), localTime);
    }

    @Test
    public void getStartTime_validInput_returnsTrue() {
        OperatingHours test = new OperatingHours("0900-2200");
        LocalTime startTime = LocalTime.of(9, 0);
        assertEquals(test.getStartTime(), startTime);
    }

    @Test
    public void getEndTime_validInput_returnsTrue() {
        OperatingHours test = new OperatingHours("0900-2200");
        LocalTime endTime = LocalTime.of(22, 0);
        assertEquals(test.getEndTime(), endTime);
    }

    @Test
    public void equals_sameOperatingHours_returnsTrue() {
        String operatingHours = "0900-2200";
        OperatingHours test = new OperatingHours(operatingHours);
        OperatingHours other = new OperatingHours(operatingHours);
        assertTrue(test.equals(other));
    }

    @Test
    public void hashCode_sameObject_returnsTrue() {
        String operatingHours = "0900-2200";
        OperatingHours test = new OperatingHours(operatingHours);
        assertEquals(test.hashCode(), Objects.hash(LocalTime.of(9, 0),
                LocalTime.of(22, 0)));
    }

    @Test
    public void toString_validInput_returnsTrue() {
        String operatingHours = "0900-2200";
        OperatingHours test = new OperatingHours(operatingHours);
        assertEquals(test.toString(), operatingHours);
    }

    @Test
    public void getDisplayedMessage_validInput_returnsCorrectMessage() {
        String operatingHours = "0900-2200";
        String expected = "09:00-22:00";
        OperatingHours test = new OperatingHours(operatingHours);
        assertEquals(test.getDisplayedMessage(), expected);
    }
}
```
###### \java\seedu\ptman\model\outlet\OutletContactTest.java
``` java
public class OutletContactTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new OutletContact(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new OutletContact(invalidPhone));
    }

    @Test
    public void isValidOutletContact_nullValue_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> OutletContact.isValidOutletContact(null));
    }

    @Test
    public void isValidOutletContact_invalidValues_returnsFalse() {
        assertFalse(OutletContact.isValidOutletContact("")); // empty string
        assertFalse(OutletContact.isValidOutletContact(" ")); // spaces only
        assertFalse(OutletContact.isValidOutletContact("91")); // less than 3 numbers
        assertFalse(OutletContact.isValidOutletContact("phone")); // non-numeric
        assertFalse(OutletContact.isValidOutletContact("9011p041")); // alphabets within digits
        assertFalse(OutletContact.isValidOutletContact("9312 1534")); // spaces within digits
    }

    @Test
    public void isValidOutletContact_validValues_returnsTrue() {
        assertTrue(OutletContact.isValidOutletContact("911")); // exactly 3 numbers
        assertTrue(OutletContact.isValidOutletContact("93121534"));
        assertTrue(OutletContact.isValidOutletContact("124293842033123")); // long phone numbers
    }
}
```
###### \java\seedu\ptman\model\outlet\OutletEmailTest.java
``` java
public class OutletEmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new OutletEmail(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new OutletEmail(invalidEmail));
    }

    @Test
    public void isValidOutletEmail_nullValue_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> OutletEmail.isValidOutletEmail(null));
    }

    @Test
    public void isValidOutletEmail_blankEmails_returnsFalse() {
        assertFalse(OutletEmail.isValidOutletEmail("")); // empty string
        assertFalse(OutletEmail.isValidOutletEmail(" ")); // spaces only
    }

    @Test
    public void isValidOutletEmail_missingParts_returnsFalse() {
        assertFalse(OutletEmail.isValidOutletEmail("@example.com")); // missing local part
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee.com")); // missing '@' symbol
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@")); // missing domain name
    }

    @Test
    public void isValidOutletEmail_invalidParts_returnsFalse() {
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@-")); // invalid domain name
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@exam_ple.com")); // underscore in domain name
        assertFalse(OutletEmail.isValidOutletEmail("cool coffee@example.com")); // spaces in local part
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@exam ple.com")); // spaces in domain name
        assertFalse(OutletEmail.isValidOutletEmail(" coolcoffee@example.com")); // leading space
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@example.com ")); // trailing space
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@@example.com")); // double '@' symbol
        assertFalse(OutletEmail.isValidOutletEmail("cool@coffee@example.com")); // '@' symbol in local part
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@example@com")); // '@' symbol in domain name
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@.example.com")); // domain starts with period
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@example.com.")); // domain ends with a period
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@-example.com")); // domain starts with a hyphen
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@example.com-")); // domain ends with a hyphen
    }

    @Test
    public void isValidOutletEmail_validEmail_returnsTrue() {
        assertTrue(OutletEmail.isValidOutletEmail("CoolCoffee_3433@example.com"));
        assertTrue(OutletEmail.isValidOutletEmail("a@bc"));  // minimal
        assertTrue(OutletEmail.isValidOutletEmail("test@localhost"));   // alphabets only
        assertTrue(OutletEmail.isValidOutletEmail("!#$%&'*+/=?`{|}~^.-@example.org")); // special characters
        assertTrue(OutletEmail.isValidOutletEmail("123@145"));  // numeric local part and domain name
        // mixture of alphanumeric and special characters
        assertTrue(OutletEmail.isValidOutletEmail("a1+be!@example1.com"));
        // long domain name
        assertTrue(OutletEmail.isValidOutletEmail("cool_coffee@very-very-very-long-example.com"));
        // long local part
        assertTrue(OutletEmail.isValidOutletEmail("if.you.dream.it_you.can.do.it@example.com"));
    }
}
```
###### \java\seedu\ptman\model\outlet\OutletInformationTest.java
``` java
public class OutletInformationTest {

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(null, operatingHours,
                outletContact, outletEmail, announcement, password, false));
    }

    @Test
    public void constructor_nullOperatingHours_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name,
                null, outletContact, outletEmail, announcement, password, false));
    }

    @Test
    public void constructor_nullOutletContact_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name,
                operatingHours, null, outletEmail, announcement, password, false));
    }

    @Test
    public void constructor_nullOutletEmail_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name, operatingHours,
                outletContact, null, announcement, password, false));
    }

    @Test
    public void constructor_nullPassword_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name, operatingHours,
                outletContact, outletEmail, announcement, null, false));
    }

    @Test
    public void constructor_nullAnnouncement_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name, operatingHours,
                outletContact, outletEmail, null, password, false));
    }

    @Test
    public void equals_sameOutletInformation_returnsTrue() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        OutletInformation outlet = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                announcement, password, false);
        OutletInformation other = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                announcement, password, false);
        assertTrue(outlet.equals(other));
    }

    @Test
    public void setAnnouncement_validAnnouncement_success() {
        OutletInformation outlet = new OutletInformation();
        Announcement editedAnnouncement = new Announcement("Edited Announcement.");
        Announcement expectedAnnouncement = new Announcement("Edited Announcement.");
        outlet.setAnnouncement(editedAnnouncement);
        assertEquals(outlet.getAnnouncement(), expectedAnnouncement);
    }

    @Test
    public void setEncryptionMode_inputTrue_returnsTrue() {
        OutletInformation outlet = new OutletInformation();
        outlet.setEncryptionMode(true);
        assertTrue(outlet.getEncryptionMode());
    }

    @Test
    public void getEncryptionModeMessage_inputTrue_returnsEncryptedMessage() {
        OutletInformation outlet = new OutletInformation();
        outlet.setEncryptionMode(true);
        assertEquals(outlet.getEncryptionModeMessage(), OutletInformation.DATA_ENCRYPTED_MESSAGE);
    }

    @Test
    public void getEncryptionModeMessage_inputFalse_returnsNotEncryptedMessage() {
        OutletInformation outlet = new OutletInformation();
        assertEquals(outlet.getEncryptionModeMessage(), OutletInformation.DATA_NOT_ENCRYPTED_MESSAGE);
    }

    @Test
    public void hashCode_sameObject_returnsTrue() {
        Password masterPassword = new Password();
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Announcement announcement = new Announcement("New Announcement.");
        OutletInformation outlet = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                announcement, masterPassword, false);
        assertEquals(outlet.hashCode(), Objects.hash(name, masterPassword, operatingHours, outletContact,
                outletEmail, announcement, false));
    }

    @Test
    public void toString_validInput_returnsTrue() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New announcement.");
        OutletInformation outlet = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                announcement, password, false);
        String expected = "Outlet Name: outlet Operating Hours: 09:00-22:00 "
                + "Contact: 91234567 Email: outlet@gmail.com Announcement: New announcement. "
                + "Encryption: Local storage files are not encrypted.";
        assertEquals(outlet.toString(), expected);
    }
}
```
###### \java\seedu\ptman\model\PartTimeManagerTest.java
``` java
    @Test
    public void encryptLocalStorage_dataNotEncrypted_encryptSuccessfully() {
        partTimeManager.encryptLocalStorage();
        assertTrue(partTimeManager.getOutletInformation().getEncryptionMode());
    }

    @Test
    public void decryptLocalStorage_dataEncrypted_decryptSuccessfully() {
        partTimeManager.encryptLocalStorage();
        partTimeManager.decryptLocalStorage();
        assertFalse(partTimeManager.getOutletInformation().getEncryptionMode());
    }
```
###### \java\seedu\ptman\storage\StorageManagerTest.java
``` java
    @Test
    public void outletInformationReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlOutletInformationStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlOutletInformationStorageTest} class.
         */
        OutletInformation original = new OutletInformation();
        storageManager.saveOutletInformation(original);
        OutletInformation retrieved = storageManager.readOutletInformation().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void getOutletInformationFilePath() {
        assertNotNull(storageManager.getOutletInformationFilePath());
    }

    @Test
    public void handleOutletDataChangedEvent_exceptionThrown_eventRaised() {
        // Create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlPartTimeManagerStorage("dummy"),
                new JsonUserPrefsStorage("dummy"),
                new XmlOutletInformationStorageExceptionThrowingStub("dummy"));
        storage.handleOutletDataChangedEvent(new OutletDataChangedEvent(new OutletInformation()));
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof DataSavingExceptionEvent);
    }

    @Test
    public void handleOutletDataChangedEvent_validInput_eventRaised() throws Exception {
        OutletInformation original = new OutletInformation();
        XmlOutletInformationStorage outletInformationStorage = new XmlOutletInformationStorage("dummy");
        Storage storage = new StorageManager(new XmlPartTimeManagerStorage("dummy"),
                new JsonUserPrefsStorage("dummy"),
                outletInformationStorage);
        storage.handleOutletDataChangedEvent(new OutletDataChangedEvent(original));
        OutletInformation readBack = outletInformationStorage
                .readOutletInformation("dummy").get();
        assertEquals(original, readBack);
    }

    @Test
    public void backupPartTimeManager_nullValue_exceptionThrown() throws Exception {
        thrown.expect(NullPointerException.class);
        storageManager.backupPartTimeManager(null);
    }

    @Test
    public void backupPartTimeManager_validValue_success() throws Exception {
        PartTimeManager original = getTypicalPartTimeManager();
        storageManager.backupPartTimeManager(original);
        XmlPartTimeManagerStorage partTimeManagerStorage = new XmlPartTimeManagerStorage(getTempFilePath("ab"));
        ReadOnlyPartTimeManager readBack =
                partTimeManagerStorage.readPartTimeManager(false, getTempFilePath("ab.backup")).get();
        assertEquals(original, new PartTimeManager(readBack));
    }

    @Test
    public void backupOutletInformation_nullValue_exceptionThrown() throws Exception {
        thrown.expect(NullPointerException.class);
        storageManager.backupOutletInformation(null);
    }

    @Test
    public void backupOutletInformation_validValue_success() throws Exception {
        OutletInformation original = new OutletInformation();
        storageManager.backupOutletInformation(original);
        XmlOutletInformationStorage outletInformationStorage =
                new XmlOutletInformationStorage(getTempFilePath("outlet"));
        OutletInformation readBack = outletInformationStorage
                .readOutletInformation(getTempFilePath("outlet.backup")).get();
        assertEquals(original, readBack);
    }

```
###### \java\seedu\ptman\storage\XmlAdaptedOutletInformationTest.java
``` java
public class XmlAdaptedOutletInformationTest {
    private static final String DECRYPTED = OutletInformation.DATA_NOT_ENCRYPTED_MESSAGE;
    private static final String ENCRYPTED = OutletInformation.DATA_ENCRYPTED_MESSAGE;
    private static final String INVALID_OUTLET_NAME = "Awesome@outlet";
    private static final String INVALID_OPERATING_HOURS = "1000/2000";
    private static final String INVALID_OUTLET_CONTACT = "+6591112222";
    private static final String INVALID_OUTLET_EMAIL = "example.com";

    private static final String VALID_OUTLET_NAME = "AwesomeOutlet";
    private static final String VALID_OPERATING_HOURS = "1000-2000";
    private static final String VALID_OUTLET_CONTACT = "91112222";
    private static final String VALID_OUTLET_EMAIL = "AwesomeOutlet@gmail.com";
    private static final String VALID_ANNOUNCEMENT = "New Announcement";
    private static final String DEFAULT_PASSWORD_HASH = new Password().getPasswordHash();

    private OutletInformation outlet = new OutletInformation();

    @Test
    public void toModelType_validOutletInformationDetails_returnsOutletInformation() throws Exception {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(outlet);
        assertEquals(outlet, outletInformation.toModelType());
    }

    @Test
    public void toModelType_invalidOutletName_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(DECRYPTED,
                INVALID_OUTLET_NAME, VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL,
                DEFAULT_PASSWORD_HASH, VALID_ANNOUNCEMENT);
        String expectedMessage = OutletName.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullOutletName_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(DECRYPTED, null,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = String.format(FAIL_MESSAGE, OutletName.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_invalidOperatingHours_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(DECRYPTED,
                VALID_OUTLET_NAME, INVALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL,
                DEFAULT_PASSWORD_HASH, VALID_ANNOUNCEMENT);
        String expectedMessage = OperatingHours.MESSAGE_OPERATING_HOUR_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullOperatingHours_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(DECRYPTED, VALID_OUTLET_NAME,
                null, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = String.format(FAIL_MESSAGE, OperatingHours.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_invalidOutletContact_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(DECRYPTED, VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, INVALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = OutletContact.MESSAGE_OUTLET_CONTACT_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullOutletContact_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(DECRYPTED, VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, null, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = String.format(FAIL_MESSAGE, OutletContact.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_invalidOutletEmail_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(DECRYPTED, VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, INVALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = OutletEmail.MESSAGE_OUTLET_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullOutletEmail_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(DECRYPTED, VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, null, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = String.format(FAIL_MESSAGE, OutletEmail.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullPasswordHash_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(DECRYPTED, VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, null,
                VALID_ANNOUNCEMENT);
        String expectedMessage = String.format(FAIL_MESSAGE, Password.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullAnnouncement_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(DECRYPTED, VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                null);
        String expectedMessage = String.format(FAIL_MESSAGE, Announcement.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void equals() {
        XmlAdaptedOutletInformation firstOutletInformation = new XmlAdaptedOutletInformation(DECRYPTED,
                VALID_OUTLET_NAME, VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL,
                DEFAULT_PASSWORD_HASH, VALID_ANNOUNCEMENT);
        XmlAdaptedOutletInformation secondOutletInformation = new XmlAdaptedOutletInformation(outlet);

        // Same object -> return true
        assertTrue(firstOutletInformation.equals(firstOutletInformation));

        // Same values -> return true
        XmlAdaptedOutletInformation firstOutletInformationCopy = new XmlAdaptedOutletInformation(DECRYPTED,
                VALID_OUTLET_NAME, VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL,
                DEFAULT_PASSWORD_HASH, VALID_ANNOUNCEMENT);
        assertTrue(firstOutletInformation.equals(firstOutletInformationCopy));

        // Different types -> return false
        assertFalse(firstOutletInformation.equals(1));

        // Null type -> return false
        assertFalse(firstOutletInformation.equals(null));

        // Different values -> return false
        assertFalse(firstOutletInformation.equals(secondOutletInformation));
    }

    @Test
    public void setAttributesFromSource_validInputs_returnsEqualObject() {
        OutletInformation outlet = new OutletInformation();
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation();
        outletInformation.setAttributesFromSource(outlet);
        XmlAdaptedOutletInformation sameOutletInformation = new XmlAdaptedOutletInformation();
        sameOutletInformation.setAttributesFromSource(outlet);
        assertEquals(outletInformation, sameOutletInformation);
    }

    @Test
    public void setAttributesFromStrings_validInputs_returnEqualObject() {
        OutletInformation outlet = new OutletInformation();
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation();
        outletInformation.setAttributesFromStrings(DECRYPTED, outlet.getName().toString(),
                outlet.getOperatingHours().toString(), outlet.getOutletContact().toString(),
                outlet.getOutletEmail().toString(), outlet.getMasterPassword().getPasswordHash(),
                outlet.getAnnouncement().toString());
        XmlAdaptedOutletInformation sameOutletInformation = new XmlAdaptedOutletInformation();
        sameOutletInformation.setAttributesFromStrings(DECRYPTED, outlet.getName().toString(),
                outlet.getOperatingHours().toString(),
                outlet.getOutletContact().toString(), outlet.getOutletEmail().toString(),
                outlet.getMasterPassword().getPasswordHash(),
                outlet.getAnnouncement().toString());
        assertEquals(outletInformation, sameOutletInformation);
    }
}
```
###### \java\seedu\ptman\storage\XmlOutletInformationStorageTest.java
``` java
public class XmlOutletInformationStorageTest {
    private static final String TEST_DATA_FOLDER =
            FileUtil.getPath("./src/test/data/XmlOutletInformationStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readOutletInformation_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readOutletInformation(null);
    }

    private java.util.Optional<OutletInformation> readOutletInformation(String filePath) throws Exception {
        return new XmlOutletInformationStorage(filePath).readOutletInformation(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readOutletInformation("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {
        thrown.expect(DataConversionException.class);
        readOutletInformation("NotXmlFormatOutletInformation.xml");
    }

    @Test
    public void readOutletInformation_invalidOutletInformation_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readOutletInformation("invalidOutletInformation.xml");
    }

    @Test
    public void readAndSaveOutletInformation_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempOutletInformation.xml";
        OutletInformation original = new OutletInformation();
        XmlOutletInformationStorage xmlOutletInformationStorage = new XmlOutletInformationStorage(filePath);

        //Save in new file and read back
        xmlOutletInformationStorage.saveOutletInformation(original, filePath);
        OutletInformation readBack = xmlOutletInformationStorage.readOutletInformation(filePath).get();
        assertEquals(original, readBack);

        //Modify data, overwrite existing file, and read back
        original.setAnnouncement(new Announcement("new announcement"));
        original.setOutletInformation(new OutletName("newName"), new OperatingHours("1000-2000"),
                new OutletContact("92223333"), new OutletEmail("newOutlet@gmail.com"));
        xmlOutletInformationStorage.saveOutletInformation(original, filePath);
        readBack = xmlOutletInformationStorage.readOutletInformation(filePath).get();
        assertEquals(original, readBack);

        //Save and read without specifying file path
        original.setAnnouncement(new Announcement("new announcement"));
        original.setOutletInformation(new OutletName("newName"), new OperatingHours("1000-2000"),
                new OutletContact("92223333"), new OutletEmail("newOutlet@gmail.com"));
        xmlOutletInformationStorage.saveOutletInformation(original);
        readBack = xmlOutletInformationStorage.readOutletInformation().get();
        assertEquals(original, readBack);
    }

    @Test
    public void saveOutletInformation_nullOutletInformation_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveOutletInformation(null, "SomeFile.xml");
    }

    @Test
    public void saveOutletInformation_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveOutletInformation(new OutletInformation(), null);
    }

    /**
     * Saves {@code outletInformation} at the specified {@code filePath}.
     */
    private void saveOutletInformation(OutletInformation outletInformation, String filePath) {
        try {
            new XmlOutletInformationStorage(filePath)
                    .saveOutletInformation(outletInformation, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void backupOutletInformation_nullOutletInformation_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        backupOutletInformation(null, TEST_DATA_FOLDER + "SomeBackupFile.xml");
    }

    @Test
    public void backupOutletInformation_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        backupOutletInformation(new OutletInformation(), null);
    }

    @Test
    public void backupOutletInformation_validInputs_backupSuccess() throws Exception {
        String filePath = TEST_DATA_FOLDER + "backupFile";
        OutletInformation original = new OutletInformation();
        backupOutletInformation(original, filePath);
        XmlOutletInformationStorage xmlOutletInformationStorage =
                new XmlOutletInformationStorage(filePath + ".backup");
        OutletInformation readBack =
                xmlOutletInformationStorage.readOutletInformation(filePath + ".backup").get();
        assertEquals(original, new OutletInformation(readBack));
    }

    /**
     * Backups {@code partTimeManager} at the specified {@code filePath}.
     */
    private void backupOutletInformation(OutletInformation outletInformation, String filePath) {
        try {
            new XmlOutletInformationStorage(filePath).backupOutletInformation(outletInformation);
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }
}
```
###### \java\seedu\ptman\storage\XmlPartTimeManagerStorageTest.java
``` java
    @Test
    public void backupPartTimeManager_nullPartTimeManager_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        backupPartTimeManager(null, TEST_DATA_FOLDER + "SomeBackupFile.xml");
    }

    @Test
    public void backupPartTimeManager_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        backupPartTimeManager(new PartTimeManager(), null);
    }

    @Test
    public void backupParTimeManager_validInputs_backupSuccess() throws Exception {
        String filePath = TEST_DATA_FOLDER + "backupFile";
        PartTimeManager original = getTypicalPartTimeManager();
        backupPartTimeManager(original, filePath);
        XmlPartTimeManagerStorage xmlPartTimeManagerStorage =
                new XmlPartTimeManagerStorage(filePath + ".backup");
        ReadOnlyPartTimeManager readBack =
                xmlPartTimeManagerStorage.readPartTimeManager(false, filePath + ".backup").get();
        assertEquals(original, new PartTimeManager(readBack));
    }

    /**
     * Backups {@code partTimeManager} at the specified {@code filePath}.
     */
    private void backupPartTimeManager(ReadOnlyPartTimeManager partTimeManager, String filePath) {
        try {
            new XmlPartTimeManagerStorage(filePath).backupPartTimeManager(partTimeManager);
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }
}
```
