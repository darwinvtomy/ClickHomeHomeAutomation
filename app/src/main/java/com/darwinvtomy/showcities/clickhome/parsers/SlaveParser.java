package com.darwinvtomy.showcities.clickhome.parsers;

import com.darwinvtomy.showcities.clickhome.model.Slave;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class SlaveParser {

    public static List<Slave> parseFeed(String content) {
        try {
            boolean inDataItemTag = false;
            String currentTagName = "";
            Slave slave = null;

            List<Slave> slavelist = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("Slave")) {
                            inDataItemTag = true;
                            slave = new Slave();
                            slavelist.add(slave);

                        }

                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("Slave")) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && slave != null) {

                            switch (currentTagName) {
                                case "id":
                                    slave.setId(Integer.parseInt(parser.getText()));
                                    break;
                                case "mid":
                                    slave.setMid(parser.getText());
                                    break;
                                case "sid":
                                    slave.setSid(parser.getText());
                                    break;
                                case "sname":
                                    slave.setName(parser.getText());
                                    break;
                                case "type":
                                    slave.setType(parser.getText());
                                    break;
                                case "status":
                                    slave.setStatus(parser.getText());
                                    break;
                                case "value":
                                    slave.setValue(parser.getText());
                                    break;
                                case "enable":
                                    slave.setEnable(parser.getText());
                                    break;
                                case "time":
                                    slave.setTime(parser.getText());
                                    break;
                                case "alarmstatus":
                                    slave.setAlarmstatus(parser.getText());
                                    break;
                                case "img":
                                    slave.setImg(parser.getText());
                                    break;
                                case "off_time":
                                    slave.setOf_time(parser.getText());
                                    break;
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
            return slavelist;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }


    }

}
