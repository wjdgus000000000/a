import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:naemansan/services/mypage_api_service.dart';

class Settings extends StatefulWidget {
  const Settings({Key? key}) : super(key: key);

  @override
  _SettingsState createState() => _SettingsState();
}

class _SettingsState extends State<Settings> {
  bool isNotificationEnabled = true;
  late Future<Map<String, dynamic>?> user;
  static const storage = FlutterSecureStorage();
  dynamic userInfo = '';

  // Fetch user info
  Future<Map<String, dynamic>?> fetchUserInfo() async {
    ProfileApiService apiService = ProfileApiService();
    return await apiService.getUserInfo();
  }

  @override
  void initState() {
    super.initState();
    user = fetchUserInfo();

    WidgetsBinding.instance.addPostFrameCallback((_) {
      checkUserState();
    });
  }

  Future<void> logout() async {
    await deleteTokens();
    await storage.delete(key: 'login');
    goLogin();
  }

  checkUserState() async {
    userInfo = await storage.read(key: 'login');
    if (userInfo == null) {
      print('로그인 페이지로 이동');
      goLogin();
    } else {
      print('로그인 중');
    }
  }

  goLogin() {
    Navigator.pushNamedAndRemoveUntil(context, '/login', (route) => false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        automaticallyImplyLeading: false,
        elevation: 2,
        foregroundColor: Colors.black87,
        backgroundColor: Colors.white,
        title: Row(
          children: [
            IconButton(
              icon: const Icon(
                Icons.arrow_back_ios_outlined,
                color: Colors.black,
              ),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            Padding(
              padding: const EdgeInsets.only(left: 5.0),
              child: Row(
                children: const [
                  Text(
                    '설정',
                    style: TextStyle(
                      fontSize: 21,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  SizedBox(width: 8),
                ],
              ),
            ),
          ],
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(10.0),
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.only(left: 18), //gma
              child: Row(
                children: [
                  const Text(
                    '알림',
                    style: TextStyle(color: Colors.black, fontSize: 18),
                  ),
                  const Spacer(),
                  // 이 부분 추가햐
                  Switch(
                    value: isNotificationEnabled,
                    onChanged: (bool value) {
                      setState(() {
                        isNotificationEnabled = value;
                        // 개인정보에 알림 설정 정보 저장
                      });
                      // 개인정보에 알림 설정 정보 저장
                    },
                    activeColor: const Color.fromARGB(255, 100, 208, 103),
                    inactiveTrackColor:
                        const Color.fromARGB(255, 226, 220, 220),
                  )
                ],
              ),
            ),
            const Divider(),
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Row(
                children: [
                  TextButton(
                    // 스토어 연결
                    onPressed: () {},
                    style: TextButton.styleFrom(
                      foregroundColor: Colors.black,
                      textStyle: const TextStyle(
                        fontSize: 18,
                      ),
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: const [
                        Text(
                          '버전 정보',
                          style: TextStyle(
                            color: Colors.black,
                            fontSize: 18,
                          ),
                        ),
                        Text(
                          '1.0.0', // 현재 버전 표시
                          style: TextStyle(
                            color: Colors.grey,
                            fontSize: 14,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            const Divider(),
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Row(
                children: [
                  TextButton(
                    onPressed: () {},
                    style: TextButton.styleFrom(
                      foregroundColor: Colors.black,
                      textStyle: const TextStyle(
                        fontSize: 18,
                      ),
                    ),
                    child: const Text('문의하기'), //고객센터 ? 이메일 ? 연결
                  ),
                ],
              ),
            ),
            const Divider(),
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Row(
                children: [
                  TextButton(
                    onPressed: () {
                      logout();
                    },
                    style: TextButton.styleFrom(
                      foregroundColor: Colors.black,
                      textStyle: const TextStyle(
                        fontSize: 18,
                      ),
                    ),
                    child: const Text('로그아웃'), // 마이페이지에 있는거 여기로 옮겨
                  ),
                ],
              ),
            ),
            const Divider(),
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Row(
                children: [
                  TextButton(
                    onPressed: () {},
                    style: TextButton.styleFrom(
                      foregroundColor: Colors.red,
                      textStyle: const TextStyle(
                        fontSize: 18,
                      ),
                    ),
                    child: const Text('내만산 탈퇴하기'),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> deleteTokens() async {
    await storage.delete(key: 'accessToken');
    await storage.delete(key: 'refreshToken');
    print("삭제 진행함?");

    final prefs = await SharedPreferences.getInstance();
    prefs.setBool('isLogged', false);
  }
}
