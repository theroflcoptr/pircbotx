package org.pircbotx.hooks;

import org.pircbotx.hooks.events.MessageEvent;
import org.apache.commons.lang3.mutable.MutableObject;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.managers.GenericListenerManager;
import org.pircbotx.hooks.managers.ListenerManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
public class TemporaryListenerTest {
	protected PublicPircBotX bot;
	protected ListenerManager listenerManager;

	@BeforeMethod
	public void setup() {
		bot = new PublicPircBotX();
		bot.setListenerManager(listenerManager = new GenericListenerManager());
	}

	@Test
	public void eventDispatched() {
		final MutableObject<MessageEvent> mutableEvent = new MutableObject();
		bot.getListenerManager().addListener(new TemporaryListener(bot) {
			@Override
			public void onMessage(MessageEvent event) throws Exception {
				super.onMessage(event);
			}
		});

		//Send some arbitrary line
		bot.handleLine(":AUser!~ALogin@some.host PRIVMSG #aChannel :Some very long message");
		MessageEvent mevent = mutableEvent.getValue();

		//Verify event contents
		assertNotNull(mevent, "MessageEvent not dispatched");
		assertEquals(mevent.getChannel(), "#aChannel", "Event channel and origional channel do not match");
		assertEquals(mevent.getUser(), "aUser", "Event user and origional user do not match");
		assertEquals(mevent.getMessage(), "Some very long message", "Message sent does not match");
	}

	public static class PublicPircBotX extends PircBotX {
		/**
		 * Since we need this for testing but aren't in the right package
		 * make it public
		 */
		@Override
		public void handleLine(String line) {
			super.handleLine(line);
		}
	}
}