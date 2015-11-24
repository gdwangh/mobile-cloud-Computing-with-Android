1. import android-support-v7-appcompat和android design support，注意要：
   a. add 到build path中
   b. export v4和v7，design
   c. 不要export dependence,
   d. build时，level要选和ide环境一致的。

2. android design support依赖v7，而且要输出要编译为libary（勾选isLibary）

3. 在student project中，在build path - lib中，根据需要选择v7，student4还需要design。
4. 重新build之前，要clean
