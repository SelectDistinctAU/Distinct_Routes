# BBBike REST API v0.2 Specification & Technical Reference

**Reference Source:** [https://www.bbbike.org/api.html](https://www.bbbike.org/api.html)  
**Maintained by:** Select Distinct  

---

## Base API Endpoint
```text
https://api.bbbike.org/api/0.2/{city}/
```
*Note:* For Perth routing, the path is `https://api.bbbike.org/api/0.2/Perth/`.

---

## Query Parameters & Formats

| Parameter | Type | Mandatory? | Value / Format | Notes & Rules |
| :--- | :--- | :--- | :--- | :--- |
| `appid` | String | **Yes** | `guest` (testing) / registered ID | **Omitting fails HTTP request** |
| `startc_wgs84` | Coordinates | **Yes** | `{lon},{lat}` | **Longitude first!** (e.g. `115.8988,-32.0016`) |
| `zielc_wgs84` | Coordinates | **Yes** | `{lon},{lat}` | **Longitude first!** (e.g. `115.8814,-31.9642`) |
| `pref_seen` | Integer | **Yes** | `1` or `2` | **Mandatory (omitting rejects request)** |
| `viac_wgs84` | Coordinates | No | `{lon},{lat}\|...` | Optional intermediate waypoints (`lon,lat`) |
| `pref_cat` | String | No | `N1` | **Prefer side streets / residential roads** |
| `pref_speed` | Integer | No | `20` | Preferred cruising speed in km/h |
| `pref_ampel` | String | No | `yes` | Avoid traffic lights |
| `pref_unlit` | String | No | `NL` | Avoid unlit streets |
| `pref_green` | String | No | `GR1` / `GR2` | Prefer green/scenic routes |
| `pref_ferry` | String | No | `use` | Permit ferries |
| `output_as` | String | No | `json` or `gpx-route` | Format of returned route data |

---

## Sample Verified Request URL (Perth)

```text
https://api.bbbike.org/api/0.2/Perth/?appid=guest&startc_wgs84=115.8988,-32.0016&zielc_wgs84=115.8814,-31.9642&pref_cat=N1&pref_speed=20&pref_seen=1&output_as=json
```

---

## Critical Rules to Avoid Failures

1. **Coordinate Order:** Always send `startc_wgs84` and `zielc_wgs84` in **`lon,lat`** order. Sending `lat,lon` will cause routing errors.
2. **Mandatory `pref_seen=1`:** Omitting `pref_seen=1` causes BBBike to reject the request.
3. **Mandatory `appid`:** Must provide `appid=guest` (or registered application ID).
4. **Side Street Weighting (`pref_cat=N1`):** Passing `pref_cat=N1` instructs BBBike's Perth Perl graph engine to prefer quiet residential roads over busy arterials.
