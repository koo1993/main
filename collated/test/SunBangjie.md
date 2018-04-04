# SunBangJie
###### \java\seedu\ptman\logic\parser\PartTimeManagerParserTest.java
``` java
    @Test
    public void parseCommand_editoutlet() throws Exception {
        String name = "EditedOutlet";
        String operatingHours = "10:00-17:00";
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
        String operatingHours = "10:00-17:00";
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

```
