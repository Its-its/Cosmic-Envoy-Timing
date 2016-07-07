package net.itstjf.envoy;

import org.lwjgl.util.Point;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiSomething extends GuiScreen {
	public static int x = 2;
	public static int y = 2;
	public static boolean showIngame = true;
	
	Point lastPoint;
	GuiButton button;
	
	public void initGui() {
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		button = new GuiButton(0, 2, res.getScaledHeight() - 22, "Toggle text ingame.");
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(lastPoint != null) {
			GuiSomething.x = mouseX - lastPoint.getX();
			GuiSomething.y = mouseY - lastPoint.getY();
		}
		
		button.drawButton(mc, mouseX, mouseY);
		
		Gui.drawRect(GuiSomething.x - 1, GuiSomething.y - 1, GuiSomething.x + getStringWidth(), GuiSomething.y + 9, 0x22000000);
	}
	
	public void mouseClicked(int x, int y, int b) {
		if(x >= GuiSomething.x && y >= GuiSomething.y && x < GuiSomething.x + getStringWidth() && y < GuiSomething.y + 10) {
			lastPoint = new Point(x - GuiSomething.x, y - GuiSomething.y);
			return;
		}
		
		if(button.mousePressed(mc, x, y)) {
			GuiSomething.showIngame = !GuiSomething.showIngame;
		}
	}
	
	public void mouseReleased(int x, int y, int b) {
		lastPoint = null;
	}
	
	public int getStringWidth() {
		return this.fontRendererObj.getStringWidth(CosmicEnvoy.instance.name);
	}
	
	public void onGuiClosed() {
		ConfigurationHandler.configuration.getCategory("options").get("x").set(GuiSomething.x);
		ConfigurationHandler.configuration.getCategory("options").get("y").set(GuiSomething.y);
		ConfigurationHandler.configuration.getCategory("options").get("showIngame").set(GuiSomething.showIngame);
		ConfigurationHandler.configuration.save();
	}
}