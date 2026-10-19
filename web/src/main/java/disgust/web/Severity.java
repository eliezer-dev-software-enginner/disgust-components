package disgust.web;

import disgust.io.Material;

/** Shared status tone for Disgust web-style compositions. */
public enum Severity {
    INFO, SUCCESS, WARNING, ERROR;

    public Material.Severity material() { return Material.Severity.valueOf(name()); }
}
