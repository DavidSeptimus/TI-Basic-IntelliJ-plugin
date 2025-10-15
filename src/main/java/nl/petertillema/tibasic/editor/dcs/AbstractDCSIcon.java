package nl.petertillema.tibasic.editor.dcs;

import com.intellij.util.ui.GraphicsUtil;
import com.intellij.util.ui.JBCachingScalableIcon;

import java.awt.*;
import java.util.List;

public abstract class AbstractDCSIcon extends JBCachingScalableIcon<AbstractDCSIcon> {

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        var cfg = GraphicsUtil.setupAAPainting(g);
        var g2 = (Graphics2D) g;
        // Crisp pixel art
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        var maxPixels = this.getSize() * this.getSize();
        for (int i = 0; i < maxPixels; i++) {
            var idx = this.getData(i);
            if (idx < 0 || idx >= 16) idx = 0;
            if (idx == 0) continue; // transparent pixel, leave background as-is
            var px = i % this.getSize();
            var py = i / this.getSize();
            g2.setColor(this.getPalette().get(idx));
            g2.fillRect(x + px, y + py, 1, 1);
        }

        cfg.restore();
    }

    protected abstract int getSize();

    protected abstract int getData(int index);

    protected abstract List<Color> getPalette();

    @Override
    public int getIconHeight() {
        return this.getSize();
    }

    @Override
    public int getIconWidth() {
        return this.getSize();
    }
}
