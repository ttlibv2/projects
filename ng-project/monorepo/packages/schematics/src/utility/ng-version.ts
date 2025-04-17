import { RunnerFactory } from '@ngdev/devkit-core/runners';

export async function ngVersion(versionCheck: string | null, warning?: boolean): Promise<string | null> {
    const ngVersion = await RunnerFactory.angular().version();
    if (ngVersion !== versionCheck && (!!warning && versionCheck != null)) {
        throw new Error(`Angular version project < Global Version`);
    }
    return ngVersion;
}