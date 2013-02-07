package org.jenkinsci.plugins.fxcop_runner;

import java.io.IOException;

import org.jenkinsci.plugins.fxcop_runner.Messages;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.EnvironmentSpecific;
import hudson.model.Hudson;
import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.slaves.NodeSpecific;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;

/**
* @author Yasuyuki Saito
*/
public final class FxCopInstallation extends ToolInstallation implements NodeSpecific<FxCopInstallation>, EnvironmentSpecific<FxCopInstallation> {

    /** */
    private transient String pathToFxCop;

    @DataBoundConstructor
    public FxCopInstallation(String name, String home) {
        super(name, home, null);
    }

    public FxCopInstallation forNode(Node node, TaskListener log) throws IOException, InterruptedException {
        return new FxCopInstallation(getName(), translateFor(node, log));
    }

    public FxCopInstallation forEnvironment(EnvVars environment) {
        return new FxCopInstallation(getName(), environment.expand(getHome()));
    }

    protected Object readResolve() {
        if (this.pathToFxCop != null) {
            return new FxCopInstallation(this.getName(), this.pathToFxCop);
        }
        return this;
    }

    /**
     * @author Yasuyuki Saito
     */
    @Extension
    public static class DescriptorImpl extends ToolDescriptor<FxCopInstallation> {

        public String getDisplayName() {
            return Messages.FxCopInstallation_DisplayName();
        }

        @Override
        public FxCopInstallation[] getInstallations() {
            return Hudson.getInstance().getDescriptorByType(FxCopBuilder.DescriptorImpl.class).getInstallations();
        }

        @Override
        public void setInstallations(FxCopInstallation... installations) {
            Hudson.getInstance().getDescriptorByType(FxCopBuilder.DescriptorImpl.class).setInstallations(installations);
        }

    }
}
